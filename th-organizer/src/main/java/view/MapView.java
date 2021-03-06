package view;

import controller.THOrganizer;
import controller.THOrganizer$;
import domain.POI;
import domain.TreasureHunt;
import domain.TreasureHunt$;
import domain.messages.Message;
import domain.messages.Message$;
import domain.messages.msgType;
import javafx.application.Platform;
import play.api.libs.json.Json;
import utils.Resources;
import utils.Strings;
import view.component.GoogleMapsPanel;
import view.component.LogoPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapView extends JFrame implements OrganizerView {

    private static final Dimension FRAME_DIMENSION = new Dimension(1024, 768);
    private static final int DEFAULT_INSET_VALUE = 10;

    private LogoPanel logoPanel;
    private final List<JButton> buttonList = new ArrayList();
    private GoogleMapsPanel googleMapsPanel;
    private final GoogleMapsFXMLController googleMapsController;
    private TreasureHunt currentTreasureHunt;
    private final THOrganizer controller;
    private POI selectedPOI;
    private JScrollPane existingPOIScrollPane;
    private JList existingPOIList;

    public MapView(final TreasureHunt currentTreasureHunt, final THOrganizer controller) {
        THOrganizer$.MODULE$.instance().model().addObserver(this);
        this.currentTreasureHunt = currentTreasureHunt;
        this.controller = controller;
        this.googleMapsController = new GoogleMapsFXMLController(currentTreasureHunt);
        this.controller.setTHRunning(currentTreasureHunt.ID());
        init();
    }

    private void init() {
        this.getContentPane().setBackground(Color.white);
        this.setLayout(new GridBagLayout());
        this.setTitle(this.currentTreasureHunt.name());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(FRAME_DIMENSION);
        this.setResizable(false);
        this.initComponent();
        this.setLocation(100, 0);
        this.setVisible(true);

        Platform.runLater(() -> this.googleMapsPanel.initFX());
    }

    private void initComponent() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE);
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.CENTER;

        this.logoPanel = new LogoPanel(LogoPanel.Position.LEFT);
        this.add(this.logoPanel, c);

        JButton createCodeButton = new JButton(Strings.CREATE_HUNT_CODE_BUTTON);
        createCodeButton.setPreferredSize(new Dimension(400, 45));
        createCodeButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> {
            Integer code = this.controller.getCode();
            JFrame dialog = new JFrame();
            JOptionPane.showMessageDialog(dialog, "Your code is: " + code + "\nGive it to the players");
        }));
        this.buttonList.add(createCodeButton);
        JButton startHuntButton = new JButton(Strings.START_HUNT_BUTTON);
        startHuntButton.setPreferredSize(new Dimension(400, 45));
        startHuntButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> {
            List<POI> poisList = scala.collection.JavaConversions.seqAsJavaList(controller.getPois());
            if (poisList.size() != 0) {
                this.controller.startHunt();
                JFrame dialog = new JFrame();
                JOptionPane.showMessageDialog(dialog, "Treasure Hunt started.\nDon't forget to create a code.");
            } else {
                JFrame dialog = new JFrame();
                JOptionPane.showMessageDialog(dialog, "You can't start a TH without POI.\nPlease add one.");
            }
        }));
        this.buttonList.add(startHuntButton);

        c.anchor = GridBagConstraints.LINE_START;
        this.buttonList.forEach(button -> {
            button.setFont(Resources.DEFAULT_FONT);
            c.gridy++;
            this.add(button, c);
        });
        if (this.currentTreasureHunt.location() == null) {
            this.buttonList.forEach(button -> button.setEnabled(false));
        }

        this.googleMapsPanel = new GoogleMapsPanel(this.googleMapsController);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx++;
        c.gridy = 0;
        c.gridheight = 2;
        this.add(this.googleMapsPanel, c);

        existingPOIScrollPane = new JScrollPane(existingPOIList);
        c.gridy += 2;
        existingPOIScrollPane.setPreferredSize(new Dimension(300, 150));
        this.add(existingPOIScrollPane, c);
        refreshPoisListPane();

        JButton deletePoiButton = new JButton("DELETE");
        deletePoiButton.setPreferredSize(new Dimension(300, 45));
        deletePoiButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> {
            //elimino il marker
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    googleMapsController.deleteMarker(controller.getPoiMarker(selectedPOI).get());
                }
            });
            //elimino il poi dal DB
            this.controller.deletePoi(this.selectedPOI);
            //elimino il poi dalla lista riassociandole la nuova lista senza poi
            refreshPoisListPane();
        }));
        c.gridheight = 1;
        c.gridy += 2;
        this.add(deletePoiButton, c);
        c.gridy -= 2;
    }

    private ArrayList<POI> convertList(scala.collection.Seq<POI> seq) {
        return new ArrayList<>(scala.collection.JavaConversions.seqAsJavaList(seq));
    }

    private void refreshPoisListPane() {
        this.existingPOIList = new JList(convertList(controller.getPois()).toArray());
        existingPOIList.addListSelectionListener(arg -> {
            if (!arg.getValueIsAdjusting()) {
                this.selectedPOI = (POI) existingPOIList.getSelectedValue();
            }
        });
        existingPOIList.setCellRenderer(new THListCellRenderer());
        existingPOIList.setFixedCellWidth(50);
        existingPOIList.setVisibleRowCount(20);
        existingPOIList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        existingPOIScrollPane.setViewportView(existingPOIList);
        existingPOIScrollPane.repaint();
    }

    @Override
    public void receiveUpdate(final String message) {
        Message msg = Json.parse(message).as(Message$.MODULE$.messageReads());
        String payload = msg.payload();
        if (msg.messageType().equals(msgType.TreasureHunt())) {
            this.buttonList.forEach(button -> button.setEnabled(true));
            this.currentTreasureHunt = Json.parse(payload).as(TreasureHunt$.MODULE$.thReads());
        }
        if (msg.messageType().equals(msgType.Poi())) {
            refreshPoisListPane();
        }
    }
}

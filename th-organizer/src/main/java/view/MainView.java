package view;

import controller.THOrganizer;
import utils.Resources;
import utils.Strings;
import view.component.LogoPanel;

import javax.swing.*;
import java.awt.*;

/**
 * This is the Main Window of the Organizer Desktop app.
 */
public class MainView extends JFrame implements OrganizerView {

    public static final Dimension FRAME_DIMENSION = new Dimension(600, 600);
    public static final int DEFAULT_INSET_VALUE = 10;
    public static final int LIST_ROW_VISIBLE = 10;

    private JButton newTHButton;
    private JLabel existingTHLabel;
    private JScrollPane existingTHScrollPane;
    private JList existingTHList;
    private LogoPanel logoPanel;
    private JButton loadButton, deleteButton;
    private final THOrganizer controller;

    public MainView(final THOrganizer controller) {
        init();
        this.controller = controller;
    }

    private void init() {
        this.getContentPane().setBackground(Color.white);
        this.setLayout(new GridBagLayout());
        this.setTitle(Strings.ORGANIZER_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_DIMENSION);
        this.setResizable(false);
        this.initComponent();
        this.setVisible(true);
    }

    private void initComponent() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE);
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.logoPanel = new LogoPanel(LogoPanel.Position.CENTER);
        this.add(this.logoPanel, c);

        this.newTHButton = new JButton(Strings.NEW_TREASURE_HUNT_BUTTON.toUpperCase());
        c.gridy++;
        c.fill = GridBagConstraints.CENTER;
        this.add(this.newTHButton, c);

        this.existingTHLabel = new JLabel(Strings.EXISTING_TH_LABEL);
        c.gridy++;
        this.add(this.existingTHLabel, c);

        this.existingTHList = new JList();
        this.existingTHList.setFixedCellWidth(50);
        this.existingTHList.setCellRenderer(new THListCellRenderer());
        this.existingTHList.setVisibleRowCount(LIST_ROW_VISIBLE);
        this.existingTHList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.existingTHScrollPane = new JScrollPane(this.existingTHList);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy++;
        this.add(this.existingTHScrollPane, c);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.white);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE);
        c1.weightx = 1;
        c1.gridx = 0;
        c1.gridy = 0;

        this.loadButton = new JButton(Strings.LOAD_BUTTON);
        this.loadButton.setBackground(Color.blue);
        this.loadButton.setForeground(Color.white);
        this.loadButton.setOpaque(true);
        this.loadButton.setBorderPainted(false);
        buttonPanel.add(this.loadButton, c1);

        this.deleteButton = new JButton(Strings.DELETE_BUTTON);
        this.deleteButton.setBackground(Color.red);
        this.deleteButton.setForeground(Color.white);
        this.deleteButton.setOpaque(true);
        this.deleteButton.setBorderPainted(false);
        c1.gridx++;
        buttonPanel.add(this.deleteButton, c1);

        c.gridy++;
        this.add(buttonPanel, c);

        this.deleteButton.setFont(Resources.DEFAULT_FONT);
        this.loadButton.setFont(Resources.DEFAULT_FONT);

    }

    @Override
    public void receiveUpdate(final String update) {
        // TODO method implementation
        try {
            throw new Exception("Not implemented method");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




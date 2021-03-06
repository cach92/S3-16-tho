package dboperation

import java.sql.Connection

import domain.{TreasureHunt, TreasureHuntImpl}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

import scala.collection.mutable.ListBuffer

trait TreasureHuntDB {
    /**
      * Method to insert a new treasure hunt in the DB
      *
      * @param name        name of treasure hunt
      * @param location    place where treasure hunt will be played
      * @param startDate   date of start
      * @param startTime   time of start
      * @param idOrganizer identifier of the organizer of the treasure hunt
      * @return identifier of the Treasure Hunt
      */
    def insertNewTreasureHunt(name: String, location: String, startDate: String, startTime: String, idOrganizer: Int): Int


    /**
      * Method to view the list of TH of an Organizer
      *
      * @param idOrganizer identifier of the Organizer
      * @return a List of Treasure Hunt
      */
    def viewTreasureHuntListByOrganizer(idOrganizer: Int): List[TreasureHunt]

    /**
      * Method to view the list of TH ready to start
      *
      * @return a List of Treasure Hunt ready to start
      */
    def viewTreasureHuntListByPlayer(): List[TreasureHunt]

    /**
      * Method to delete a treasure hunt
      *
      * @param treasureHunt identifier of the treasureHunt
      */
    def deleteTreasureHunt(treasureHunt: TreasureHunt): Unit = {

    }
}

case class TreasureHuntDBImpl() extends TreasureHuntDB {
    /**
      * Method to insert a new treasure hunt in the DB
      *
      * @param name        name of treasure hunt
      * @param location    place where treasure hunt will be played
      * @param startDate   date of start
      * @param startTime   time of start
      * @param idOrganizer identifier of the organizer of the treasure hunt
      * @return identifier of the Treasure Hunt
      **/
    override def insertNewTreasureHunt(name: String, location: String, startDate: String, startTime: String, idOrganizer: Int): Int = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        var query = s"INSERT INTO treasure_hunt (name, location, start_date, start_time, id_organizer) VALUES ('${name}','${location}','${startDate}','${startTime}', ${idOrganizer})"
        statement.executeUpdate(query)
        query = s"SELECT MAX(id_treasure_hunt) FROM treasure_hunt"
        val rs = statement.executeQuery(query)
        var idNewTH = 0
        while (rs.next) {
            idNewTH = rs.getInt(1)
        }
        connection.close()
        idNewTH
    }

    /**
      * Method to view the list of TH of an Organizer
      */
    override def viewTreasureHuntListByOrganizer(idOrganizer: Int) = {
        var thList: ListBuffer[TreasureHunt] = ListBuffer.empty
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"SELECT * FROM treasure_hunt " +
            s"WHERE id_organizer = $idOrganizer " +
            s"AND id_treasure_hunt NOT IN " +
            s"(SELECT id_treasure_hunt " +
            s"FROM event_log " +
            s"WHERE event_type = 6)"
        val rs = statement.executeQuery(query)
        while (rs.next) {
            thList += TreasureHuntImpl(rs.getInt("id_treasure_hunt"), rs.getString("name"), rs.getString("location"), rs.getString("start_date"), rs.getString("start_time"))
        }
        connection.close()
        thList.toList
    }

    override def viewTreasureHuntListByPlayer(): List[TreasureHunt] = {
        var thList: ListBuffer[TreasureHunt] = ListBuffer.empty
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"SELECT * FROM treasure_hunt " +
            s"WHERE  id_treasure_hunt IN " +
            s"(SELECT id_treasure_hunt " +
            s"FROM event_log " +
            s"WHERE event_type = 6)" +
            s"AND id_treasure_hunt NOT IN " +
            s"(SELECT id_treasure_hunt " +
            s"FROM event_log " +
            s"WHERE event_type = 5)"
        val rs = statement.executeQuery(query)
        while (rs.next) {
            thList += TreasureHuntImpl(rs.getInt("id_treasure_hunt"), rs.getString("name"), rs.getString("location"), rs.getString("start_date"), rs.getString("start_time"))
        }
        connection.close()
        thList.toList
    }
}

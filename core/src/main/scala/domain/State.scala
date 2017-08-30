package domain

import domain.messages.StateType.StateType
import play.api.libs.json.Json

trait State extends Serializable {

    def state: StateType

    def treasureHuntID: String

}


case class StateImpl(override val state: StateType, override val treasureHuntID: String) extends State {

    implicit val stateWrites = Json.writes[StateImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object State {

    def apply(state: StateType, treasureHuntID: String): StateImpl = {
        StateImpl(state, treasureHuntID)
    }

}
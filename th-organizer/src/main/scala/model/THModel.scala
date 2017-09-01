package model

import core.Observable
import domain.messages.PoiMsgImpl
import domain.{POI, Quiz, TreasureHunt}

trait THModel extends Observable[String] {

    def broker: Broker

    def broker_=(broker: Broker): Unit

    def addTreasureHunt(th: TreasureHunt): Unit

    def getTreasureHunts: Seq[TreasureHunt]

    def addQuiz(quiz: Quiz): Unit

    def getQuizzes: Seq[Quiz]

    def addPOI(poi: POI): Unit

    def getPOIs: Seq[POI]
}

class THModelImpl(override var broker: Broker) extends THModel {

    require(broker != null)

    private val organizerID = ""

    private var ths: Seq[TreasureHunt] = Seq empty
    private var quizzes: Seq[Quiz] = Seq empty
    private var pois: Seq[POI] = Seq empty

    override def addTreasureHunt(th: TreasureHunt): Unit = {
        require(ths != null && !ths.contains(th))
        broker send th.ID
        ths = ths :+ th
    }

    override def getTreasureHunts: Seq[TreasureHunt] = ths

    override def addQuiz(quiz: Quiz): Unit = {
        require(quizzes != null && !quizzes.contains(quiz))
        quizzes = quizzes :+ quiz
    }

    override def getQuizzes: Seq[Quiz] = quizzes

    override def addPOI(poi: POI): Unit = {
        require(pois != null && !pois.contains(poi))
        broker send (PoiMsgImpl(organizerID, poi defaultRepresentation) defaultRepresentation)
        pois = pois :+ poi
    }

    override def getPOIs: Seq[POI] = pois

}
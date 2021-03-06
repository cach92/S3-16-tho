package domain

import org.scalatest.{BeforeAndAfter, FunSuite}

class POITest extends FunSuite with BeforeAndAfter {

    val position = PositionImpl(1.2, 3.0)
    val quiz = QuizImpl(question = "This is a question?", answer = "This is the answer")
    val clue = ClueImpl(content = "The POI is nearby the church")
    val name = "POI-0"
    val treasureHuntID = 0
    var poi: POI = _

    before {
        poi = POIImpl(position = position, name = name, treasureHuntID = treasureHuntID)
    }

    test("Getting the POI's name") {
        assert(poi.name === name)
    }

    test("Getting the Quiz") {
        assert(poi.quiz === null)
        poi = POIImpl(position = position, name = name, treasureHuntID = treasureHuntID, _quiz = quiz)
        assert(poi.quiz === quiz)
    }

    test("Getting the Clue") {
        assert(poi.clue === null)
        poi = POIImpl(position = position, name = name, treasureHuntID = treasureHuntID, _clue = clue)
        assert(poi.quiz === null)
        assert(poi.clue === clue)
    }

    test("Setting a Clue") {
        poi.clue = clue
        assert(poi.clue === clue)
    }

    test("Setting a Quiz") {
        poi.quiz = quiz
        assert(poi.quiz === quiz)
    }

}

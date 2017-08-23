package domain

import org.scalatest.FunSuite;

class TreasureHuntTest extends FunSuite {

    val thID = "TH-0"
    val th = TreasureHuntImpl(thID)
    val teamName = "TestTeam"
    val team = TeamImpl(teamName, null)

    test("Getting Treasure Hunt's ID") {
        assert(th.ID === thID)
    }

    test("Initially the Treasure Hunt could have no player") {
        assert(th.teams isEmpty)
    }

    test("Checking the addition of a Team") {
        th addTeam team
        assert(!th.teams.isEmpty)
        assert(th.teams(0) === team)
        assertThrows[IndexOutOfBoundsException] {
            th teams 1
        }
    }

    test("Getting a Team by a given name") {
        assert(th.team(teamName) === team)
    }

    test("Getting a Team that is not enrolled in the Treasure Hunt") {
        assert(th.team("newTeam") === null)
    }

}
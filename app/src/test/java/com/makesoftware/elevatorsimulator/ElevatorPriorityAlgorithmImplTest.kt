package com.makesoftware.elevatorsimulator

import org.junit.Test

class ElevatorPriorityAlgorithmImplTest {

    private val elevatorPriorityAlgorithm = ElevatorPriorityAlgorithmImpl()

    // TODO: Add more tests:
    // - Test for going down
    // - Test for stopped

    @Test
    fun sortFloorsToGoUp() {
        val currentFloorsToGo = mutableListOf(4, 1, 3)
        val currentFloor = 2

        val sortedFloorsToGo = elevatorPriorityAlgorithm.sortFloorsToGo(
            currentFloorsToGo, currentFloor
        )

        assert(sortedFloorsToGo == mutableListOf(3, 4, 1))
    }

    @Test
    fun sortFloorsToGoUp2() {
        val currentFloorsToGo = mutableListOf(3, 2, 4)
        val currentFloor = 1

        val sortedFloorsToGo = elevatorPriorityAlgorithm.sortFloorsToGo(
            currentFloorsToGo, currentFloor
        )

        assert(sortedFloorsToGo == mutableListOf(2, 3, 4))
    }

    @Test
    fun sortFloorsToGoDown() {
//        val currentFloorsToGo = mutableListOf(3, 2, 4)
//        val currentFloor = 1
//
//        val sortedFloorsToGo = elevatorPriorityAlgorithm.sortFloorsToGo(
//            currentFloorsToGo, currentFloor
//        )
//
//        assert(sortedFloorsToGo == mutableListOf(2, 3, 4))
    }
}
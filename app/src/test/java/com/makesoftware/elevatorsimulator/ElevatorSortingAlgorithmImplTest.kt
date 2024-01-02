package com.makesoftware.elevatorsimulator

import com.makesoftware.elevatorsimulator.algorithms.ElevatorSortingAlgorithmImpl
import org.junit.Test

class ElevatorSortingAlgorithmImplTest {

    private val elevatorPriorityAlgorithm = ElevatorSortingAlgorithmImpl()

    @Test
    fun sortFloorsGoingUp() {
        val currentFloorsToGo = mutableListOf(4, 1, 3)
        val currentFloor = 2

        val sortedFloorsToGo = elevatorPriorityAlgorithm.sortFloorsToGo(
            currentFloorsToGo, currentFloor
        )

        assert(sortedFloorsToGo == mutableListOf(3, 4, 1))
    }

    @Test
    fun sortFloorsGoingUp2() {
        val currentFloorsToGo = mutableListOf(3, 2, 4)
        val currentFloor = 1

        val sortedFloorsToGo = elevatorPriorityAlgorithm.sortFloorsToGo(
            currentFloorsToGo, currentFloor
        )

        assert(sortedFloorsToGo == mutableListOf(2, 3, 4))
    }

    @Test
    fun sortFloorsGoingDown() {
        val currentFloorsToGo = mutableListOf(1, 4, 3)
        val currentFloor = 2

        val sortedFloorsToGo = elevatorPriorityAlgorithm.sortFloorsToGo(
            currentFloorsToGo, currentFloor
        )

        assert(sortedFloorsToGo == mutableListOf(1, 3, 4))
    }

    @Test
    fun sortFloorsGoingDown2() {
        val currentFloorsToGo = mutableListOf(0, 4, 3)
        val currentFloor = 1

        val sortedFloorsToGo = elevatorPriorityAlgorithm.sortFloorsToGo(
            currentFloorsToGo, currentFloor
        )

        assert(sortedFloorsToGo == mutableListOf(0, 3, 4))
    }

    @Test
    fun sortAlreadySortedFloors() {
        val currentFloorsToGo = mutableListOf(1, 2, 3, 4)
        val currentFloor = 1

        val sortedFloorsToGo = elevatorPriorityAlgorithm.sortFloorsToGo(
            currentFloorsToGo, currentFloor
        )

        assert(sortedFloorsToGo == mutableListOf(1, 2, 3, 4))
    }
}
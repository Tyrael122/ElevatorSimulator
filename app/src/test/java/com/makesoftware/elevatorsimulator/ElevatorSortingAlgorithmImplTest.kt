package com.makesoftware.elevatorsimulator

import com.makesoftware.elevatorsimulator.algorithms.ElevatorSortingAlgorithmImpl
import org.junit.Test

class ElevatorSortingAlgorithmImplTest {

    private val elevatorPriorityAlgorithm = ElevatorSortingAlgorithmImpl()

    @Test
    fun sortFloorsGoingUpAscendingOrderAndBackDown() {
        val currentFloorQueue = mutableListOf(4, 1, 3)
        val currentFloor = 2
        val elevatorDirection = ElevatorDirection.UP

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor, elevatorDirection
        )

        assert(sortedFloorQueue == mutableListOf(3, 4, 1))
    }

    @Test
    fun sortFloorsGoingUpAscendingOrder() {
        val currentFloorQueue = mutableListOf(3, 2, 4)
        val currentFloor = 1
        val elevatorDirection = ElevatorDirection.UP

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor, elevatorDirection
        )

        assert(sortedFloorQueue == mutableListOf(2, 3, 4))
    }

    @Test
    fun sortFloorsGoingUpAndBackDown() {
        val currentFloorQueue = mutableListOf(3, 1)
        val currentFloor = 2
        val elevatorDirection = ElevatorDirection.UP

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor, elevatorDirection
        )

        assert(sortedFloorQueue == mutableListOf(3, 1))
    }

    @Test
    fun sortFloorsGoingDownAndComeBackUpAscendingOrder() {
        val currentFloorQueue = mutableListOf(1, 4, 3)
        val currentFloor = 2
        val elevatorDirection = ElevatorDirection.DOWN

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor, elevatorDirection
        )

        assert(sortedFloorQueue == mutableListOf(1, 3, 4))
    }

    @Test
    fun sortFloorsGoingDownDescendingOrder() {
        val currentFloorQueue = mutableListOf(2, 3)
        val currentFloor = 3
        val elevatorDirection = ElevatorDirection.DOWN

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor, elevatorDirection
        )

        assert(sortedFloorQueue == mutableListOf(3, 2))
    }

    @Test
    fun sortAlreadySortedFloors() {
        val currentFloorQueue = mutableListOf(1, 2, 3, 4)
        val currentFloor = 1
        val elevatorDirection = ElevatorDirection.UP

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor, elevatorDirection
        )

        assert(sortedFloorQueue == mutableListOf(1, 2, 3, 4))
    }
}
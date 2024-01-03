package com.makesoftware.elevatorsimulator

import com.makesoftware.elevatorsimulator.algorithms.ElevatorSortingAlgorithmImpl
import org.junit.Test

class ElevatorSortingAlgorithmImplTest {

    private val elevatorPriorityAlgorithm = ElevatorSortingAlgorithmImpl()

    @Test
    fun sortFloorsGoingUp() {
        val currentFloorQueue = mutableListOf(4, 1, 3)
        val currentFloor = 2

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor
        )

        assert(sortedFloorQueue == mutableListOf(3, 4, 1))
    }

    @Test
    fun sortFloorsGoingUp2() {
        val currentFloorQueue = mutableListOf(3, 2, 4)
        val currentFloor = 1

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor
        )

        assert(sortedFloorQueue == mutableListOf(2, 3, 4))
    }

    @Test
    fun sortFloorsGoingUp3() {
        val currentFloorQueue = mutableListOf(3, 1)
        val currentFloor = 2

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor
        )

        assert(sortedFloorQueue == mutableListOf(3, 1))
    }

//    @Test
//    fun sortFloorsGoingUp4() {
//        val currentFloorQueue = mutableListOf(3, 2)
//        val currentFloor = 3
//
//        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
//            currentFloorQueue, currentFloor
//        )
//
//        assert(sortedFloorQueue == mutableListOf(3, 2))
//    }

    @Test
    fun sortFloorsGoingDown() {
        val currentFloorQueue = mutableListOf(1, 4, 3)
        val currentFloor = 2

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor
        )

        assert(sortedFloorQueue == mutableListOf(1, 3, 4))
    }

    @Test
    fun sortFloorsGoingDown2() {
        val currentFloorQueue = mutableListOf(0, 4, 3)
        val currentFloor = 1

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor
        )

        assert(sortedFloorQueue == mutableListOf(0, 3, 4))
    }

    @Test
    fun sortAlreadySortedFloors() {
        val currentFloorQueue = mutableListOf(1, 2, 3, 4)
        val currentFloor = 1

        val sortedFloorQueue = elevatorPriorityAlgorithm.sortFloorsQueue(
            currentFloorQueue, currentFloor
        )

        assert(sortedFloorQueue == mutableListOf(1, 2, 3, 4))
    }
}
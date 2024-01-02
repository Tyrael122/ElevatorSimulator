package com.makesoftware.elevatorsimulator

import org.junit.Test

class ElevatorPriorityAlgorithmImplTest {

    @Test
    fun sortFloorsToGo() {
        val elevatorPriorityAlgorithm = ElevatorPriorityAlgorithmImpl()

        val currentFloorsToGo = mutableListOf(4, 1, 3)
        val currentDirection = ElevatorDirection.UP
        val currentFloor = 2

        elevatorPriorityAlgorithm.sortFloorsToGo(currentFloorsToGo, currentDirection, currentFloor)

        assert(currentFloorsToGo == mutableListOf(3, 4, 1))
    }
}
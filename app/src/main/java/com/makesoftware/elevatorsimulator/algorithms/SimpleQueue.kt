package com.makesoftware.elevatorsimulator.algorithms

import com.makesoftware.elevatorsimulator.ElevatorDirection

class SimpleQueue : ElevatorSortingAlgorithm {
    override fun sortFloorsQueue(
        floorQueue: List<Int>, currentFloor: Int, elevatorDirection: ElevatorDirection
    ): List<Int> {
        return floorQueue
    }
}
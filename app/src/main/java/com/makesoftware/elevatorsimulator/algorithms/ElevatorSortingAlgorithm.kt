package com.makesoftware.elevatorsimulator.algorithms

import com.makesoftware.elevatorsimulator.ElevatorDirection

interface ElevatorSortingAlgorithm {
    fun sortFloorsQueue(
        floorQueue: List<Int>, currentFloor: Int, elevatorDirection: ElevatorDirection
    ): List<Int>
}

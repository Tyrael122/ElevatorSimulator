package com.makesoftware.elevatorsimulator.algorithms

interface ElevatorSortingAlgorithm {
    fun sortFloorsQueue(floorQueue: List<Int>, currentFloor: Int): List<Int>
}

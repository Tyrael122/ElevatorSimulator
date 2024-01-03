package com.makesoftware.elevatorsimulator

interface ElevatorSortingAlgorithm {
    fun sortFloorsQueue(floorQueue: List<Int>, currentFloor: Int): List<Int>
}

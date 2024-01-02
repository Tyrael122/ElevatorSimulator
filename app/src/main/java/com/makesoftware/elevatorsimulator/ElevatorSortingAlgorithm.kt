package com.makesoftware.elevatorsimulator

interface ElevatorSortingAlgorithm {
    fun sortFloorsToGo(floorsToGo: List<Int>, currentFloor: Int): List<Int>
}

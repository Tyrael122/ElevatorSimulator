package com.makesoftware.elevatorsimulator

interface ElevatorPriorityAlgorithm {
    fun sortFloorsToGo(floorsToGo: List<Int>, currentFloor: Int): List<Int>
}

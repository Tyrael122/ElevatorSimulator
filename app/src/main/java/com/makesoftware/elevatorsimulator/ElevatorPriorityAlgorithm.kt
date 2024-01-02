package com.makesoftware.elevatorsimulator

interface ElevatorPriorityAlgorithm {
    fun sortFloorsToGo(currentFloorsToGo: MutableList<Int>, currentDirection: ElevatorDirection, currentFloor: Int)
}

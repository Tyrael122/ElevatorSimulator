package com.makesoftware.elevatorsimulator

class ElevatorPriorityAlgorithmImpl : ElevatorPriorityAlgorithm {
    override fun sortFloorsToGo(
        currentFloorsToGo: MutableList<Int>,
        currentDirection: ElevatorDirection,
        currentFloor: Int
    ) {
        if (currentDirection == ElevatorDirection.UP) {
            currentFloorsToGo.sort()
        } else {
            currentFloorsToGo.sortDescending()
        }
    }
}

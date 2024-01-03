package com.makesoftware.elevatorsimulator.algorithms

import com.makesoftware.elevatorsimulator.ElevatorDirection

class ElevatorSortingAlgorithmImpl : ElevatorSortingAlgorithm {

    override fun sortFloorsQueue(
        floorQueue: List<Int>, currentFloor: Int, elevatorDirection: ElevatorDirection
    ): List<Int> {
        val goingUpFloors = filterGoingUpFloors(floorQueue, currentFloor, elevatorDirection)
        val goingDownFloors = filterGoingDownFloors(floorQueue, currentFloor, elevatorDirection)

        return when (elevatorDirection) {
            ElevatorDirection.UP -> goingUpFloors + goingDownFloors
            else -> goingDownFloors + goingUpFloors
        }
    }

    private fun filterGoingUpFloors(floorQueue: List<Int>, currentFloor: Int, elevatorDirection: ElevatorDirection): List<Int> {
        return when (elevatorDirection) {
            ElevatorDirection.UP -> floorQueue.filter { it >= currentFloor }.sorted()
            else -> floorQueue.filter { it > currentFloor }.sorted()
        }
    }

    private fun filterGoingDownFloors(floorQueue: List<Int>, currentFloor: Int, elevatorDirection: ElevatorDirection): List<Int> {
        return when (elevatorDirection) {
            ElevatorDirection.DOWN -> floorQueue.filter { it <= currentFloor }.sortedDescending()
            else -> floorQueue.filter { it < currentFloor }.sortedDescending()
        }
    }
}

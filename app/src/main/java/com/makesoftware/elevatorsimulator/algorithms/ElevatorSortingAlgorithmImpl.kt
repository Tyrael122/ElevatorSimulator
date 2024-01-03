package com.makesoftware.elevatorsimulator.algorithms

class ElevatorSortingAlgorithmImpl : ElevatorSortingAlgorithm {
    override fun sortFloorsQueue(
        floorQueue: List<Int>, currentFloor: Int
    ): List<Int> {
        val mutableFloorQueue = floorQueue.toMutableList()

        mutableFloorQueue.sort()

        val pivotIndex = findPivotIndex(mutableFloorQueue, currentFloor) ?: return mutableFloorQueue

        val isGoingUp = floorQueue.first() > currentFloor
        return balanceOnPivotPoint(mutableFloorQueue, pivotIndex, isGoingUp)
    }

    private fun findPivotIndex(
        mutableFloorQueue: MutableList<Int>, currentFloor: Int
    ): Int? {
        val firstFloorLessThanCurrent = mutableFloorQueue.find { it > currentFloor } ?: return null
        return mutableFloorQueue.indexOf(firstFloorLessThanCurrent)
    }

    private fun balanceOnPivotPoint(
        mutableFloorQueue: MutableList<Int>, pivotIndex: Int, isGoingUp: Boolean
    ): List<Int> {
        val floorsBeforePivot = mutableFloorQueue.subList(0, pivotIndex)
        val floorsAfterPivot = mutableFloorQueue.subList(pivotIndex, mutableFloorQueue.size)

        val sortedFloorQueue = mutableListOf<Int>()

        if (isGoingUp) {
            sortedFloorQueue.addAll(floorsAfterPivot)
            sortedFloorQueue.addAll(floorsBeforePivot)
        } else {
            sortedFloorQueue.addAll(floorsBeforePivot)
            sortedFloorQueue.addAll(floorsAfterPivot)
        }

        return sortedFloorQueue
    }
}

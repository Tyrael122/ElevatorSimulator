package com.makesoftware.elevatorsimulator

class ElevatorPriorityAlgorithmImpl : ElevatorPriorityAlgorithm {
    override fun sortFloorsToGo(
        floorsToGo: List<Int>, currentFloor: Int
    ): List<Int> {
        val mutableFloorsToGo = floorsToGo.toMutableList()

        if (currentFloor < mutableFloorsToGo.first()) {
            mutableFloorsToGo.sort()
        } else {
            mutableFloorsToGo.sortDescending()
        }

        val isPivot = if (currentFloor < mutableFloorsToGo.first()) {
            { floor: Int -> floor > currentFloor }
        } else {
            { floor: Int -> floor < currentFloor }
        }

        val lastFloorGreaterThanCurrent =
            mutableFloorsToGo.find { isPivot(it) } ?: return mutableFloorsToGo
        val pivotIndex = floorsToGo.indexOf(lastFloorGreaterThanCurrent) - 1

        val floorsToGoBeforePivot = mutableFloorsToGo.subList(0, pivotIndex)
        val floorsToGoAfterPivot = mutableFloorsToGo.subList(pivotIndex, mutableFloorsToGo.size)

        val sortedFloorsToGo = mutableListOf<Int>()
        sortedFloorsToGo.addAll(floorsToGoAfterPivot)
        sortedFloorsToGo.addAll(floorsToGoBeforePivot)

        return sortedFloorsToGo
    }
}

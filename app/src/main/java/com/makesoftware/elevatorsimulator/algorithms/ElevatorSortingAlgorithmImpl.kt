package com.makesoftware.elevatorsimulator.algorithms

import com.makesoftware.elevatorsimulator.ElevatorSortingAlgorithm

class ElevatorSortingAlgorithmImpl : ElevatorSortingAlgorithm {
    override fun sortFloorsToGo(
        floorsToGo: List<Int>, currentFloor: Int
    ): List<Int> {
        val mutableFloorsToGo = floorsToGo.toMutableList()

        mutableFloorsToGo.sort()

        val pivotIndex = findPivotIndex(mutableFloorsToGo, currentFloor) ?: return mutableFloorsToGo

        val isGoingUp = floorsToGo.first() > currentFloor
        return balanceOnPivotPoint(mutableFloorsToGo, pivotIndex, isGoingUp)
    }

    private fun findPivotIndex(
        mutableFloorsToGo: MutableList<Int>, currentFloor: Int
    ): Int? {
        val firstFloorLessThanCurrent = mutableFloorsToGo.find { it > currentFloor } ?: return null
        return mutableFloorsToGo.indexOf(firstFloorLessThanCurrent)
    }

    private fun balanceOnPivotPoint(
        mutableFloorsToGo: MutableList<Int>, pivotIndex: Int, isGoingUp: Boolean
    ): List<Int> {
        val floorsToGoBeforePivot = mutableFloorsToGo.subList(0, pivotIndex)
        val floorsToGoAfterPivot = mutableFloorsToGo.subList(pivotIndex, mutableFloorsToGo.size)

        val sortedFloorsToGo = mutableListOf<Int>()

        if (isGoingUp) {
            sortedFloorsToGo.addAll(floorsToGoAfterPivot)
            sortedFloorsToGo.addAll(floorsToGoBeforePivot)
        } else {
            sortedFloorsToGo.addAll(floorsToGoBeforePivot)
            sortedFloorsToGo.addAll(floorsToGoAfterPivot)
        }

        return sortedFloorsToGo
    }
}

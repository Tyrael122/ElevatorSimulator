package com.makesoftware.elevatorsimulator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makesoftware.elevatorsimulator.algorithms.ElevatorSortingAlgorithm
import com.makesoftware.elevatorsimulator.algorithms.ElevatorSortingAlgorithmImpl
import com.makesoftware.elevatorsimulator.controllers.DoorController
import com.makesoftware.elevatorsimulator.controllers.ElevatorDoorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ElevatorViewModel(
    private val elevatorSortingAlgorithm: ElevatorSortingAlgorithm = ElevatorSortingAlgorithmImpl(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElevatorUiState())
    val uiState: StateFlow<ElevatorUiState> = _uiState.asStateFlow()

    private val doorController: DoorController =
        DoorController(this::changeDoorState, this::getDoorState)

    private val elevatorFloorAnimator: ElevatorFloorAnimator =
        ElevatorFloorAnimator(changeCurrentFloor = this::changeCurrentFloor)

    fun callElevator(floor: Int) {
        val shouldOpenDoors =
            floor == _uiState.value.currentFloor.toInt() && _uiState.value.currentDirection == ElevatorDirection.STOPPED

        if (shouldOpenDoors) {
            doorController.openDoors()
            return
        }

        addFloorToQueue(floor)
        startReadingFloorQueue()
    }

    fun onDoorFinishedMoving() {
        viewModelScope.launch {
            doorController.finishedMovingDoorsCallback()
        }

        startReadingFloorQueue()
    }

    private fun addFloorToQueue(floor: Int) {
        if (_uiState.value.floorQueue.contains(floor)) {
            return
        }

        val currentFloorQueue = _uiState.value.floorQueue.toMutableList()
        currentFloorQueue.add(floor)

        val sortedFloorQueue = elevatorSortingAlgorithm.sortFloorsQueue(
            currentFloorQueue, _uiState.value.currentFloor.toInt(), _uiState.value.currentDirection
        )

        _uiState.update {
            it.copy(floorQueue = sortedFloorQueue)
        }
    }

    private fun startReadingFloorQueue() {
        if (!doorController.isReadyToMove()) {
            return
        }

        if (_uiState.value.floorQueue.isEmpty()) {
            return
        }

        transitionToNextFloor()
    }

    private fun transitionToNextFloor() {
        val currentFloor = _uiState.value.currentFloor
        val targetFloor = calculateTargetFloor()

        Log.d("ElevatorViewModel", "Going from $currentFloor to $targetFloor")

        viewModelScope.launch {
            elevatorFloorAnimator.startAnimation(
                currentFloor,
                targetFloor,
                _uiState.value.currentDirection,
                this@ElevatorViewModel::onReachedTargetFloor
            )
        }

        _uiState.update {
            it.copy(
                currentDirection = if (currentFloor < targetFloor) {
                    ElevatorDirection.UP
                } else {
                    ElevatorDirection.DOWN
                }
            )
        }
    }

    private fun calculateTargetFloor(): Int {
        return when (_uiState.value.currentDirection) {
            ElevatorDirection.UP -> _uiState.value.floorQueue.first { it >= _uiState.value.currentFloor }
            ElevatorDirection.DOWN -> _uiState.value.floorQueue.first { it <= _uiState.value.currentFloor }
            else -> _uiState.value.floorQueue.first()
        }
    }

    private fun onReachedTargetFloor() {
        _uiState.update {
            it.copy(
                floorQueue = it.floorQueue.minus(_uiState.value.currentFloor.toInt()),
                currentDirection = ElevatorDirection.STOPPED
            )
        }

        doorController.openDoors()
    }


    private fun changeDoorState(elevatorDoorState: ElevatorDoorState) {
        _uiState.update {
            it.copy(
                doorState = elevatorDoorState
            )
        }
    }

    private fun getDoorState(): ElevatorDoorState {
        return _uiState.value.doorState
    }

    private fun changeCurrentFloor(currentFloor: Float) {
        _uiState.update {
            it.copy(
                currentFloor = currentFloor
            )
        }
    }
}

data class ElevatorUiState(
    val currentFloor: Float = 0F,
    val currentDirection: ElevatorDirection = ElevatorDirection.STOPPED,
    val doorState: ElevatorDoorState = ElevatorDoorState.CLOSED,
    val floorQueue: List<Int> = emptyList(),
)
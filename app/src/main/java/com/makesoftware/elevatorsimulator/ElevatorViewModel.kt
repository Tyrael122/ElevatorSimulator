package com.makesoftware.elevatorsimulator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makesoftware.elevatorsimulator.algorithms.ElevatorSortingAlgorithmImpl
import com.makesoftware.elevatorsimulator.controllers.DoorController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

class ElevatorViewModel(
    private val elevatorSortingAlgorithm: ElevatorSortingAlgorithm = ElevatorSortingAlgorithmImpl(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElevatorUiState())
    val uiState: StateFlow<ElevatorUiState> = _uiState.asStateFlow()

    private val doorController: DoorController =
        DoorController(this::changeDoorState, this::getDoorState)

    private val floorTravelTimeInMilliseconds = 2500

    fun callElevator(floor: Int) {
        if (floor == _uiState.value.currentFloor) {
            doorController.openDoors()
            return
        }

        addFloorToQueue(floor)
        startReadingFloorQueue()
    }

    fun elevatorHasArrived() {
        _uiState.update {
            it.copy(
                floorQueue = it.floorQueue.drop(1)
            )
        }

        doorController.openDoors()
    }

    fun elevatorHasFinishedMovingDoors() {
        viewModelScope.launch {
            doorController.finishedMovingDoorsCallback()
        }

        startReadingFloorQueue()
    }

    private fun addFloorToQueue(floor: Int) {
        val currentFloorQueue = _uiState.value.floorQueue.toMutableList()

        currentFloorQueue.add(floor)

        val sortedFloorQueue = elevatorSortingAlgorithm.sortFloorsQueue(
            currentFloorQueue, _uiState.value.currentFloor
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
            _uiState.update {
                it.copy(
                    currentDirection = ElevatorDirection.STOPPED
                )
            }

            return
        }

        goToNextFloor()
    }

    private fun goToNextFloor() {
        val currentFloor = _uiState.value.currentFloor
        val nextFloor = _uiState.value.floorQueue.first()

        _uiState.update {
            it.copy(
                currentDirection = if (currentFloor < nextFloor) {
                    ElevatorDirection.UP
                } else {
                    ElevatorDirection.DOWN
                },
                currentFloor = nextFloor,
                movementDuration = abs(currentFloor - nextFloor) * floorTravelTimeInMilliseconds
            )
        }
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
}

data class ElevatorUiState(
    val currentFloor: Int = 0,
    val movementDuration: Int = 0,
    val currentDirection: ElevatorDirection = ElevatorDirection.STOPPED,
    val doorState: ElevatorDoorState = ElevatorDoorState.CLOSED,
    val floorQueue: List<Int> = emptyList(),
)
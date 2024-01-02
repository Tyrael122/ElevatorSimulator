package com.makesoftware.elevatorsimulator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

class ElevatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ElevatorUiState())
    val uiState: StateFlow<ElevatorUiState> = _uiState.asStateFlow()

    private val elevatorFloorsPerTick = 0.1F
    private val tickTimeInMilliseconds = 100L

    private var elevatorJob: Job? = null

    fun callElevator(floor: Int) {
        addFloorToGo(floor)

        startElevator()
    }

    private fun addFloorToGo(floor: Int) {
        val currentFloorsToGo = _uiState.value.floorsToGo.toMutableList()
        currentFloorsToGo.add(floor)

        currentFloorsToGo.sortDescending()

        _uiState.update {
            it.copy(floorsToGo = currentFloorsToGo)
        }
    }

    private fun startElevator() {
        if (_uiState.value.floorsToGo.isEmpty()) {
            return
        }

        elevatorJob?.cancel()

        elevatorJob = viewModelScope.launch {
            val currentFloor = _uiState.value.currentFloor
            val nextFloor = _uiState.value.floorsToGo.first()

//            transitionToNextFloor(currentFloor, nextFloor)

            _uiState.update {
                it.copy(
                    currentDirection = if (currentFloor < nextFloor) {
                        ElevatorDirection.UP
                    } else {
                        ElevatorDirection.DOWN
                    }, currentFloor = nextFloor.toFloat(),
                    movementDuration = abs(currentFloor - nextFloor).toInt() * 2000
                )
            }
        }
    }

    fun elevatorHasArrived() {
        _uiState.update {
            it.copy(
                floorsToGo = it.floorsToGo.drop(1)
            )
        }

        if (_uiState.value.floorsToGo.isEmpty()) {
            stopElevator()
        } else {
            startElevator()
        }
    }

    private fun stopElevator() {
        _uiState.update {
            it.copy(
                currentDirection = ElevatorDirection.STOPPED
            )
        }
    }

    private suspend fun transitionToNextFloor(initialFloor: Float, nextFloor: Int) {
        val direction = if (initialFloor < nextFloor) {
            ElevatorDirection.UP
        } else {
            ElevatorDirection.DOWN
        }

        while (hasReachedNextFloor(_uiState.value.currentFloor, nextFloor, direction)) {

            _uiState.update {
                it.copy(
                    currentDirection = direction,
                    currentFloor = if (direction == ElevatorDirection.UP) {
                        _uiState.value.currentFloor + elevatorFloorsPerTick
                    } else {
                        _uiState.value.currentFloor - elevatorFloorsPerTick
                    }
                )
            }

            delay(tickTimeInMilliseconds)
        }

        _uiState.update {
            it.copy(
                currentFloor = nextFloor.toFloat()
            )
        }
    }

    private fun hasReachedNextFloor(
        currentFloor: Float, nextFloor: Int, direction: ElevatorDirection
    ): Boolean {
        return if (direction == ElevatorDirection.UP) {
            currentFloor < nextFloor
        } else {
            currentFloor > nextFloor
        }
    }
}

data class ElevatorUiState(
    val currentFloor: Float = 0F,
    val movementDuration: Int = 0,
    val currentDirection: ElevatorDirection = ElevatorDirection.STOPPED,
    val floorsToGo: List<Int> = emptyList(),
)
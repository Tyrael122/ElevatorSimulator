package com.makesoftware.elevatorsimulator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makesoftware.elevatorsimulator.algorithms.ElevatorSortingAlgorithmImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

class ElevatorViewModel(
    private val elevatorSortingAlgorithm: ElevatorSortingAlgorithm = ElevatorSortingAlgorithmImpl()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ElevatorUiState())
    val uiState: StateFlow<ElevatorUiState> = _uiState.asStateFlow()

    private val floorTravelTimeInMilliseconds = 2500
    private val timeToCloseDoors: Long = 1000
    private val timeToGatherPassengers: Long = 1000

    private var elevatorJob: Job? = null

    fun callElevator(floor: Int) {
        addFloorToGo(floor)

        startElevator()
    }

    private fun addFloorToGo(floor: Int) {
        val currentFloorsToGo = _uiState.value.floorsToGo.toMutableList()
        if (currentFloorsToGo.contains(floor)) {
            return
        }

        currentFloorsToGo.add(floor)

        val sortedFloorsToGo = elevatorSortingAlgorithm.sortFloorsToGo(
            currentFloorsToGo, _uiState.value.currentFloor
        )

        _uiState.update {
            it.copy(floorsToGo = sortedFloorsToGo)
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
    }

    fun elevatorHasArrived() {
        _uiState.update {
            it.copy(
                floorsToGo = it.floorsToGo.drop(1)
            )
        }

//        viewModelScope.launch {
//            openDoors()
//        }

        if (_uiState.value.floorsToGo.isEmpty()) {
            stopElevator()
        } else {
            startElevator()
        }
    }

    private suspend fun openDoors() {
        delay(timeToGatherPassengers + timeToCloseDoors)
    }

    private fun stopElevator() {
        _uiState.update {
            it.copy(
                currentDirection = ElevatorDirection.STOPPED
            )
        }
    }
}

data class ElevatorUiState(
    val currentFloor: Int = 0,
    val movementDuration: Int = 0,
    val currentDirection: ElevatorDirection = ElevatorDirection.STOPPED,
    val floorsToGo: List<Int> = emptyList(),
)
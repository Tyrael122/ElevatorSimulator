package com.makesoftware.elevatorsimulator.controllers

import android.util.Log
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DoorController(
    private val changeDoorState: (ElevatorDoorState) -> Unit,
    private val getDoorState: () -> ElevatorDoorState
) {

    private val timeoutToCloseDoorInMilliseconds = 2000L

    fun openDoors() {
        changeDoorState(ElevatorDoorState.OPENING)
        Log.d("DoorController", "Started animation to open doors")
    }

    suspend fun finishedMovingDoorsCallback() {
        when (getDoorState()) {
            ElevatorDoorState.OPENING -> waitToCloseDoor()
            ElevatorDoorState.CLOSING -> changeDoorState(ElevatorDoorState.CLOSED)

            else -> {
                // Do nothing
            }
        }
    }

    private suspend fun waitToCloseDoor() {
        changeDoorState(ElevatorDoorState.OPENED)

        val formattedNow =
            LocalDateTime.now().plusSeconds(3).format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))

        Log.d("DoorController", "Door has opened. Should begin closing at $formattedNow")

        delay(timeoutToCloseDoorInMilliseconds)

        Log.d("DoorController", "Going to close doors")
        closeDoors()
    }

    private fun closeDoors() {
        changeDoorState(ElevatorDoorState.CLOSING)
        Log.d("DoorController", "Started animation to close doors")
    }

    fun isReadyToMove(): Boolean {
        return getDoorState() == ElevatorDoorState.CLOSED
    }
}

enum class ElevatorDoorState {
    OPENED,
    OPENING,
    CLOSING,
    CLOSED,
}
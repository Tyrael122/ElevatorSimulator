package com.makesoftware.elevatorsimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makesoftware.elevatorsimulator.ui.theme.ElevatorSimulatorTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElevatorSimulatorTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ElevatorSimulator()
                }
            }
        }
    }
}

@Composable
fun ElevatorSimulator(modifier: Modifier = Modifier, viewModel: ElevatorViewModel = viewModel()) {
    val corridorWidth = 220.dp
    val shaftWidth = 100.dp
    val shaftHeight = 145.dp

    val uiState by viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .width(corridorWidth)
                .fillMaxHeight()
                .background(elevatorCorridorColor)
        ) {
            ElevatorCorridor(
                shaftWidth = shaftWidth,
                shaftHeight = shaftHeight,
                onFloorSelected = { floorNumber ->
                    viewModel.callElevator(floorNumber)
                },
                elevatorCurrentDirection = uiState.currentDirection,
                elevatorCurrentFloor = uiState.currentFloor
            )

            val targetElevatorBottomOffset = calculateElevatorBottomOffset(
                screenHeight = maxHeight, floor = uiState.currentFloor, shaftHeight = shaftHeight
            )

            val elevatorBottomOffset by animateFloatAsState(targetValue = targetElevatorBottomOffset.value,
                animationSpec = tween(
                    durationMillis = uiState.movementDuration, easing = EaseInOutQuad
                ),
                label = "Elevator moving animation",
                finishedListener = {
                    viewModel.elevatorHasArrived()
                })

            ElevatorCabin(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = shaftStartPadding)
                    .offset(y = -elevatorBottomOffset.dp),
                width = shaftWidth,
                height = shaftHeight - elevatorTopPadding,
            )
        }
    }
}

fun calculateElevatorBottomOffset(
    screenHeight: Dp,
    shaftHeight: Dp,
    floor: Int,
): Dp {
    val numberOfFloors = screenHeight / shaftHeight

    val totalShaftHeight = numberOfFloors.toInt() * shaftHeight.value
    val totalShaftPadding = screenHeight.value - totalShaftHeight

    val shaftHeightWithPadding =
        shaftHeight.value + (totalShaftPadding / (numberOfFloors.toInt() - 1))

    return (floor * shaftHeightWithPadding).dp
}

@Composable
fun ElevatorCorridor(
    shaftWidth: Dp,
    shaftHeight: Dp,
    onFloorSelected: (Int) -> Unit,
    elevatorCurrentDirection: ElevatorDirection,
    elevatorCurrentFloor: Int
) {
    val numberOfFloors = LocalConfiguration.current.screenHeightDp / shaftHeight.value
    val listOfFloors = (0 until numberOfFloors.toInt()).toList().reversed()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(elevatorCorridorColor)
//            .padding(top = 10.dp)
    ) {
        repeat(numberOfFloors.toInt()) {
            ElevatorFloor(
                shaftHeight = shaftHeight,
                shaftWidth = shaftWidth,
                floorNumber = listOfFloors[it],
                onFloorSelected = onFloorSelected,
                elevatorCurrentDirection = elevatorCurrentDirection,
                elevatorCurrentFloor = elevatorCurrentFloor,
            )
        }
    }
}

@Composable
fun ElevatorFloor(
    shaftHeight: Dp,
    shaftWidth: Dp,
    floorNumber: Int,
    onFloorSelected: (Int) -> Unit,
    elevatorCurrentDirection: ElevatorDirection,
    elevatorCurrentFloor: Int
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(shaftHeight)
            .fillMaxWidth()
    ) {
        ElevatorShaft(
            shaftWidth = shaftWidth,
            shaftHeight = shaftHeight,
            elevatorCurrentDirection = elevatorCurrentDirection,
            elevatorCurrentFloor = elevatorCurrentFloor
        )

        ElevatorButton(
            floorNumber = floorNumber,
            onFloorSelected = onFloorSelected,
            modifier = Modifier.padding(end = 20.dp)
        )
    }
}

@Composable
fun ElevatorShaft(
    shaftWidth: Dp,
    shaftHeight: Dp,
    elevatorCurrentDirection: ElevatorDirection,
    elevatorCurrentFloor: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(shaftWidth)
            .height(shaftHeight)
            .padding(start = shaftStartPadding)
    ) {
        ElevatorFloorIndicator(
            elevatorCurrentFloor = elevatorCurrentFloor,
            elevatorCurrentDirection = elevatorCurrentDirection,
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
fun ElevatorFloorIndicator(
    modifier: Modifier = Modifier,
    elevatorCurrentFloor: Int,
    elevatorCurrentDirection: ElevatorDirection,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(elevatorIndicatorColor),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val directionIndicatorIcon = when (elevatorCurrentDirection) {
            ElevatorDirection.UP -> Icons.Filled.KeyboardArrowUp
            ElevatorDirection.DOWN -> Icons.Filled.KeyboardArrowDown
            ElevatorDirection.STOPPED -> Icons.Filled.Menu
        }

        Icon(
            directionIndicatorIcon,
            contentDescription = null,
            tint = Color.White,
        )

        Text(
            text = elevatorCurrentFloor.toString(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }
}

@Composable
fun ElevatorButton(
    modifier: Modifier = Modifier, floorNumber: Int, onFloorSelected: (Int) -> Unit
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .size(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(elevatorButtonColor)
            .clickable { onFloorSelected(floorNumber) }) {

        val floorNumberText = if (floorNumber == 0) "T" else floorNumber.toString()
        Text(text = floorNumberText, fontWeight = FontWeight.Black, fontSize = 18.sp)
    }
}

@Composable
fun ElevatorCabin(modifier: Modifier = Modifier, width: Dp, height: Dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(width)
            .height(height)
            .background(elevatorDoorColor)
    ) {
        Divider(
            color = Color.Gray, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
    }
}

val shaftStartPadding = 20.dp
val elevatorTopPadding = 25.dp

val elevatorIndicatorColor = Color(0xFF7D7D7D)
val elevatorDoorColor = Color(0xFFC4CAD8)
val elevatorCorridorColor = Color(0xFFADADAD)
val elevatorButtonColor = Color(0xFFE0E0E0)

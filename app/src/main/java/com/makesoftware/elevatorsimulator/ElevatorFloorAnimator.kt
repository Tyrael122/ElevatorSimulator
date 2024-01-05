package com.makesoftware.elevatorsimulator

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

class ElevatorFloorAnimator(
    private val changeCurrentFloor: (Float) -> Unit
) {
    private val travelTimeInMillisecondsPerFloor = 2500

    private var animator: ValueAnimator? = null

    suspend fun startAnimation(
        initialFloor: Float,
        targetFloor: Int,
        elevatorDirection: ElevatorDirection,
        onEnd: () -> Unit = {}
    ) {
        coroutineScope {
            animator?.let {
                it.removeAllListeners()
                it.cancel()
            }

            launch {
                animator = performAnimation(
                    initialFloor, targetFloor, onEnd, resolveInterpolator(elevatorDirection)
                )
            }
        }
    }

    private fun performAnimation(
        initialFloor: Float,
        targetFloor: Int,
        onEnd: () -> Unit,
        resolvedInterpolator: TimeInterpolator
    ): ValueAnimator {
        Log.d("ElevatorFloorAnimator", "Going from $initialFloor to $targetFloor")

        val animationDuration = travelTimeInMillisecondsPerFloor * abs(initialFloor - targetFloor)

        val animator = ValueAnimator.ofFloat(initialFloor, targetFloor.toFloat()).apply {
            duration = animationDuration.toLong()
            interpolator = resolvedInterpolator
        }

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            changeCurrentFloor(animatedValue)
        }

        animator.addListener(onEnd = {
            Log.d("ElevatorFloorAnimator", "Reached target floor $targetFloor, calling onEnd")
            onEnd()
        })

        animator.start()

        return animator
    }

    private fun resolveInterpolator(elevatorDirection: ElevatorDirection): TimeInterpolator {
        return if (animator == null || elevatorDirection == ElevatorDirection.STOPPED) {
            AccelerateDecelerateInterpolator()
        } else {
            DecelerateInterpolator()
        }
    }
}

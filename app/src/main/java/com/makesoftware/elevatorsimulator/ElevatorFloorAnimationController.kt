package com.makesoftware.elevatorsimulator

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

class ElevatorFloorAnimationController(
    private val changeCurrentFloor: (Float) -> Unit
) {
    private val travelTimeInMillisecondsPerFloor = 2500

    private val animationJob: Job? = null
    private lateinit var currentElevatorDirection: ElevatorDirection

    suspend fun startAnimation(initialFloor: Float, targetFloor: Int, onEnd: () -> Unit = {}) {
        coroutineScope {
            val interpolator = resolveInterpolator(initialFloor, targetFloor)

            animationJob?.cancel()

            launch {
                performAnimation(initialFloor, targetFloor, onEnd, interpolator)
            }
        }
    }

    private fun resolveInterpolator(initialFloor: Float, targetFloor: Int): TimeInterpolator {
        if (animationJob == null) {
            return AccelerateDecelerateInterpolator()
        }

        val previousAnimationDirection = currentElevatorDirection
        currentElevatorDirection = if (initialFloor < targetFloor) {
            ElevatorDirection.UP
        } else {
            ElevatorDirection.DOWN
        }

        return if (previousAnimationDirection == currentElevatorDirection) {
            DecelerateInterpolator()
        } else {
            AnticipateInterpolator()
        }
    }

    private fun performAnimation(
        initialFloor: Float,
        targetFloor: Int,
        onEnd: () -> Unit,
        resolvedInterpolator: TimeInterpolator
    ) {
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
            onEnd()
        })

        animator.start()
    }
}

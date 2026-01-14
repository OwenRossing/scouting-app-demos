package com.owen.superalliance

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun RotaryDialCounter(
    modifier: Modifier = Modifier,
    onCountChange: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val hapticController = remember { HapticController(context) }

    var count by remember { mutableIntStateOf(0) }

    // Number of checkpoints around the circle
    val numCheckpoints = 30
    val degreesPerCheckpoint = 360f / numCheckpoints

    DisposableEffect(Unit) {
        onDispose {
            hapticController.cleanup()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Counter display
            Text(
                text = count.toString(),
                fontSize = 72.sp,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Race track
            RaceTrack(
                modifier = Modifier.size(320.dp),
                numCheckpoints = numCheckpoints,
                onCheckpointCrossed = { isLargeCheckpoint ->
                    count++
                    onCountChange(count)
                    hapticController.tick(isLargeCheckpoint)
                }
            )

            // Reset button
            androidx.compose.material3.TextButton(
                onClick = {
                    count = 0
                    onCountChange(0)
                    hapticController.reset()
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Reset")
            }
        }
    }
}

@Composable
private fun RaceTrack(
    modifier: Modifier = Modifier,
    numCheckpoints: Int,
    onCheckpointCrossed: (Boolean) -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurface = MaterialTheme.colorScheme.onSurface

    var centerX by remember { mutableFloatStateOf(0f) }
    var centerY by remember { mutableFloatStateOf(0f) }
    var lastCheckpoint by remember { mutableIntStateOf(-1) }
    var fingerPosition by remember { mutableStateOf<Offset?>(null) }

    val degreesPerCheckpoint = 360f / numCheckpoints

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        // Initialize tracking
                        val dx = offset.x - centerX
                        val dy = offset.y - centerY
                        var angle = atan2(dy, dx) * 180f / PI.toFloat()
                        if (angle < 0) angle += 360f

                        lastCheckpoint = (angle / degreesPerCheckpoint).toInt() % numCheckpoints
                        fingerPosition = offset
                    },
                    onDrag = { change, _ ->
                        change.consume()

                        val position = change.position
                        fingerPosition = position

                        val dx = position.x - centerX
                        val dy = position.y - centerY

                        // Calculate current angle (0-360)
                        var angle = atan2(dy, dx) * 180f / PI.toFloat()
                        if (angle < 0) angle += 360f

                        // Determine current checkpoint
                        val currentCheckpoint = (angle / degreesPerCheckpoint).toInt() % numCheckpoints

                        // Check if we crossed a new checkpoint
                        if (currentCheckpoint != lastCheckpoint) {
                            // Calculate if we moved forward or backward
                            val diff = (currentCheckpoint - lastCheckpoint + numCheckpoints) % numCheckpoints

                            // Only count if moving forward (less than half circle)
                            if (diff > 0 && diff <= numCheckpoints / 2) {
                                // We crossed forward, count each checkpoint crossed
                                for (i in 0 until diff) {
                                    onCheckpointCrossed(i % 5 == 0)
                                }
                            }

                            lastCheckpoint = currentCheckpoint
                        }
                    },
                    onDragEnd = {
                        fingerPosition = null
                    },
                    onDragCancel = {
                        fingerPosition = null
                    }
                )
            }
    ) {
        centerX = size.width / 2f
        centerY = size.height / 2f
        val radius = min(size.width, size.height) / 2f - 40.dp.toPx()
        val trackWidth = 60.dp.toPx()

        // Draw outer track boundary
        drawCircle(
            color = surfaceVariant,
            radius = radius + trackWidth / 2,
            center = Offset(centerX, centerY),
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw inner track boundary
        drawCircle(
            color = surfaceVariant,
            radius = radius - trackWidth / 2,
            center = Offset(centerX, centerY),
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw checkpoints around the circle
        for (i in 0 until numCheckpoints) {
            val angle = i * degreesPerCheckpoint * PI.toFloat() / 180f
            val isLargeCheckpoint = i % 5 == 0

            val checkpointRadius = if (isLargeCheckpoint) 8.dp.toPx() else 5.dp.toPx()
            val checkpointColor = if (isLargeCheckpoint) primaryColor else onSurface.copy(alpha = 0.6f)

            val x = centerX + radius * cos(angle - PI.toFloat() / 2)
            val y = centerY + radius * sin(angle - PI.toFloat() / 2)

            drawCircle(
                color = checkpointColor,
                radius = checkpointRadius,
                center = Offset(x, y)
            )
        }

        // Draw finger position indicator
        fingerPosition?.let { pos ->
            val dx = pos.x - centerX
            val dy = pos.y - centerY
            val distance = sqrt(dx * dx + dy * dy)

            // Project finger position onto the track
            val normalizedX = dx / distance
            val normalizedY = dy / distance

            val projectedX = centerX + normalizedX * radius
            val projectedY = centerY + normalizedY * radius

            // Draw glowing indicator on track
            drawCircle(
                color = primaryColor.copy(alpha = 0.3f),
                radius = trackWidth / 2 + 4.dp.toPx(),
                center = Offset(projectedX, projectedY)
            )

            drawCircle(
                color = primaryColor,
                radius = 12.dp.toPx(),
                center = Offset(projectedX, projectedY)
            )

            // Draw line from center to finger position for visual feedback
            drawLine(
                color = primaryColor.copy(alpha = 0.3f),
                start = Offset(centerX, centerY),
                end = Offset(projectedX, projectedY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Draw center hint text area
        drawCircle(
            color = surfaceVariant.copy(alpha = 0.3f),
            radius = radius - trackWidth / 2 - 8.dp.toPx(),
            center = Offset(centerX, centerY)
        )
    }
}

/**
 * Manages haptic feedback with smart intensity variation to prevent numbness
 */
class HapticController(context: Context) {
    private val vibrator: Vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    // Track recent haptic events to vary intensity
    private val recentTicks = mutableListOf<Long>()
    private var tickCount = 0
    private val maxHistoryMs = 1000L

    // Significantly increased haptic patterns - much more noticeable
    private val lightTick = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        VibrationEffect.createOneShot(15, 180)  // 15ms, strong intensity
    } else null

    private val mediumTick = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        VibrationEffect.createOneShot(18, 220) // 18ms, very strong intensity
    } else null

    private val strongTick = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        VibrationEffect.createOneShot(25, 255) // 25ms, maximum intensity
    } else null

    // Extra strong for large checkpoints
    private val extraStrongTick = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        VibrationEffect.createOneShot(35, 255) // 35ms, maximum intensity - very noticeable
    } else null

    fun tick(isLargeCheckpoint: Boolean = false) {
        val currentTime = System.currentTimeMillis()

        // Clean old ticks from history
        recentTicks.removeAll { currentTime - it > maxHistoryMs }
        recentTicks.add(currentTime)

        // Calculate ticks per second
        val ticksPerSecond = recentTicks.size.toFloat()

        tickCount++

        // Large checkpoints always get extra strong feedback
        if (isLargeCheckpoint) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && extraStrongTick != null) {
                vibrator.vibrate(extraStrongTick)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(35)
            }
            return
        }

        // For normal checkpoints, vary based on speed and pattern
        val effect = when {
            // At very high speeds (>15/sec), use lighter haptics
            ticksPerSecond > 15f -> {
                // Still vibrate every tick but lighter
                lightTick
            }
            // Alternate between medium and strong for better feel
            tickCount % 2 == 0 -> mediumTick
            else -> strongTick
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && effect != null) {
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(20)
        }
    }

    fun reset() {
        tickCount = 0
        recentTicks.clear()
    }

    fun cleanup() {
        vibrator.cancel()
    }
}

package com.owen.superalliance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owen.superalliance.ui.screens.*
import com.owen.superalliance.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Locale
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding

@Composable
fun ScoutingApp(
    viewModel: ScoutingViewModel
) {
    // Timer effect
    LaunchedEffect(viewModel.timerRunning) {
        while (viewModel.timerRunning) {
            delay(1000L)
            viewModel.tick()

            // Don't auto-switch phases while scouting.
            // If you want auto-end behavior, keep only the match end.
            if (viewModel.remainingSeconds == 0) {
                viewModel.pauseTimer()
                // Optional: comment this out too if you don't want any auto-navigation.
                viewModel.setPhase(ScoutingViewModel.Phase.EXPORT)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            // Ensure we respect status + nav bars in edge-to-edge mode.
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with timer
            ScoutingHeader(
                viewModel = viewModel,
                onStartTimer = { viewModel.startTimer() },
                onPauseTimer = { viewModel.pauseTimer() },
                onResetTimer = { viewModel.resetTimer() }
            )

            // Main content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (viewModel.currentPhase) {
                    ScoutingViewModel.Phase.PRE -> PreMatchScreen(
                        viewModel = viewModel,
                        onStartMatch = {
                            viewModel.setPhase(ScoutingViewModel.Phase.AUTO)
                            viewModel.startTimer()
                        }
                    )
                    ScoutingViewModel.Phase.AUTO -> AutoScreen(viewModel = viewModel)
                    ScoutingViewModel.Phase.TELEOP -> TeleopScreen(viewModel = viewModel)
                    ScoutingViewModel.Phase.ENDGAME -> EndgameScreen(viewModel = viewModel)
                    ScoutingViewModel.Phase.EXPORT -> ExportScreen(viewModel = viewModel)
                }
            }

            // Bottom navigation
            BottomNavigation(
                currentPhase = viewModel.currentPhase,
                onPhaseSelected = { viewModel.setPhase(it) }
            )
        }
    }
}

@Composable
fun ScoutingHeader(
    viewModel: ScoutingViewModel,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onResetTimer: () -> Unit
) {
    val matchData = viewModel.matchData

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DarkBackground.copy(alpha = 0.78f),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .blur(10.dp)
        ) {}

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title & Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "SUPERALLIANCE SCOUT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkText,
                        letterSpacing = 0.5.sp
                    )

                    val subtitle = buildString {
                        append(matchData.station?.name ?: "Unassigned")
                        if (matchData.matchNumber != null) {
                            append(" • Match ${matchData.matchNumber}")
                        }
                        if (matchData.teamNumber != null) {
                            append(" • Team ${matchData.teamNumber}")
                        }
                    }

                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = DarkMuted
                    )
                }

                // Timer
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Phase badge
                    PhasePill(
                        text = when (viewModel.currentPhase) {
                            ScoutingViewModel.Phase.PRE -> "PRE"
                            ScoutingViewModel.Phase.AUTO -> "AUTO"
                            ScoutingViewModel.Phase.TELEOP -> "TELEOP"
                            ScoutingViewModel.Phase.ENDGAME -> "END"
                            ScoutingViewModel.Phase.EXPORT -> "EXPORT"
                        },
                        color = when (viewModel.currentPhase) {
                            ScoutingViewModel.Phase.AUTO -> DarkAccent
                            ScoutingViewModel.Phase.TELEOP -> DarkGood
                            ScoutingViewModel.Phase.ENDGAME -> DarkWarn
                            else -> DarkMuted
                        }
                    )

                    // Time display
                    Text(
                        text = formatTime(viewModel.remainingSeconds),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = when {
                            viewModel.remainingSeconds <= 30 -> DarkWarn
                            viewModel.remainingSeconds <= 10 -> DarkBad
                            else -> DarkText
                        }
                    )

                    // Control buttons
                    SmallButton(
                        text = if (viewModel.timerRunning) "Pause" else "Start",
                        onClick = { if (viewModel.timerRunning) onPauseTimer() else onStartTimer() }
                    )

                    SmallButton(
                        text = "Reset",
                        onClick = onResetTimer
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigation(
    currentPhase: ScoutingViewModel.Phase,
    onPhaseSelected: (ScoutingViewModel.Phase) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DarkBackground.copy(alpha = 0.86f),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NavButton(
                label = "PRE",
                isSelected = currentPhase == ScoutingViewModel.Phase.PRE,
                onClick = { onPhaseSelected(ScoutingViewModel.Phase.PRE) },
                modifier = Modifier.weight(1f)
            )
            NavButton(
                label = "AUTO",
                isSelected = currentPhase == ScoutingViewModel.Phase.AUTO,
                onClick = { onPhaseSelected(ScoutingViewModel.Phase.AUTO) },
                modifier = Modifier.weight(1f)
            )
            NavButton(
                label = "TELEOP",
                isSelected = currentPhase == ScoutingViewModel.Phase.TELEOP,
                onClick = { onPhaseSelected(ScoutingViewModel.Phase.TELEOP) },
                modifier = Modifier.weight(1f)
            )
            NavButton(
                label = "END",
                isSelected = currentPhase == ScoutingViewModel.Phase.ENDGAME,
                onClick = { onPhaseSelected(ScoutingViewModel.Phase.ENDGAME) },
                modifier = Modifier.weight(1f)
            )
            NavButton(
                label = "EXPORT",
                isSelected = currentPhase == ScoutingViewModel.Phase.EXPORT,
                onClick = { onPhaseSelected(ScoutingViewModel.Phase.EXPORT) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun NavButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) DarkAccent.copy(alpha = 0.14f)
                else DarkPanel.copy(alpha = 0.65f)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) DarkAccent.copy(alpha = 0.95f)
                else DarkStroke.copy(alpha = 0.9f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.ExtraBold,
            color = if (isSelected) DarkText else DarkMuted,
            fontSize = 13.sp
        )
    }
}

@Composable
fun SmallButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkPanel.copy(alpha = 0.75f))
            .border(
                width = 1.dp,
                color = DarkStroke.copy(alpha = 0.9f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PhasePill(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(DarkPanel2.copy(alpha = 0.8f))
            .border(
                width = 1.dp,
                color = DarkStroke.copy(alpha = 0.9f),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = color,
            fontSize = 12.sp
        )
    }
}

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format(Locale.US, "%02d:%02d", mins, secs)
}

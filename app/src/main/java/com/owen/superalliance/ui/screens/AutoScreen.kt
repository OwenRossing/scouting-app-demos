package com.owen.superalliance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.owen.superalliance.*
import com.owen.superalliance.ui.components.*
import com.owen.superalliance.ui.theme.DarkBackground

@Composable
fun AutoScreen(
    viewModel: ScoutingViewModel,
    modifier: Modifier = Modifier
) {
    val autoData = viewModel.matchData.autoData

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScoutCard(title = "AUTONOMOUS START") {
            SectionLabel("Start Position")
            SegmentedButton(
                options = listOf("Left", "Center", "Right"),
                selectedOption = autoData.startPosition?.name?.lowercase()?.replaceFirstChar { it.uppercase() },
                onOptionSelected = {
                    viewModel.setStartPosition(
                        when(it) {
                            "Left" -> StartPosition.LEFT
                            "Center" -> StartPosition.CENTER
                            "Right" -> StartPosition.RIGHT
                            else -> null
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Auto Speed")
            SegmentedButton(
                options = listOf("Slow", "Normal", "Fast"),
                selectedOption = autoData.autoSpeed?.name?.lowercase()?.replaceFirstChar { it.uppercase() },
                onOptionSelected = {
                    viewModel.setAutoSpeed(
                        when(it) {
                            "Slow" -> AutoSpeed.SLOW
                            "Normal" -> AutoSpeed.NORMAL
                            "Fast" -> AutoSpeed.FAST
                            else -> null
                        }
                    )
                }
            )
        }

        ScoutCard(title = "AUTO ACTIONS") {
            SectionLabel("Quick Tags (tap what you see)")
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Crosses Bump",
                        isSelected = autoData.crossesBump,
                        onClick = { viewModel.toggleAutoCrossesBump() },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Intakes",
                        isSelected = autoData.intakesInAuto,
                        onClick = { viewModel.toggleAutoIntakes() },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Shoots",
                        isSelected = autoData.shootsInAuto,
                        onClick = { viewModel.toggleAutoShoots() },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Climbs",
                        isSelected = autoData.climbsInAuto,
                        onClick = { viewModel.toggleAutoClimbs() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (autoData.climbsInAuto) {
                Spacer(modifier = Modifier.height(12.dp))
                SectionLabel("Auto Climb Level (L2/L3 impossible in auto)")
                SegmentedButton(
                    options = listOf("None", "L1"),
                    selectedOption = autoData.autoClimbLevel.displayName,
                    onOptionSelected = {
                        viewModel.setAutoClimbLevel(
                            when(it) {
                                "L1" -> AutoClimbLevel.L1
                                else -> AutoClimbLevel.NONE
                            }
                        )
                    },
                    columns = 2
                )
            }
        }

        ScoutCard(title = "AUTO NOTES (OPTIONAL)") {
            SectionLabel("Brief description of auto routine")
            androidx.compose.material3.OutlinedTextField(
                value = autoData.description,
                onValueChange = { viewModel.setAutoDescription(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = {
                    androidx.compose.material3.Text("e.g., leaves zone, picks up 2, shoots, returns...")
                },
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.material3.Text(
                text = "✓ Eyes-on allowed during auto",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}


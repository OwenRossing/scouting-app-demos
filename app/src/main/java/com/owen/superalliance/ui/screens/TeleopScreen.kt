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
import com.owen.superalliance.ui.theme.*

@Composable
fun TeleopScreen(
    viewModel: ScoutingViewModel,
    modifier: Modifier = Modifier
) {
    val teleopData = viewModel.matchData.teleopData

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScoutCard(title = "SCORING PERFORMANCE") {
            SectionLabel("Rough Scoring Rate")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BucketButton(
                    label = "~1/s",
                    sublabel = "low",
                    isSelected = teleopData.shotRate == ShotRate.RATE_1,
                    onClick = { viewModel.setShotRate(ShotRate.RATE_1) },
                    modifier = Modifier.weight(1f)
                )
                BucketButton(
                    label = "~5/s",
                    sublabel = "mid",
                    isSelected = teleopData.shotRate == ShotRate.RATE_5,
                    onClick = { viewModel.setShotRate(ShotRate.RATE_5) },
                    modifier = Modifier.weight(1f)
                )
                BucketButton(
                    label = "~10/s",
                    sublabel = "high",
                    isSelected = teleopData.shotRate == ShotRate.RATE_10,
                    onClick = { viewModel.setShotRate(ShotRate.RATE_10) },
                    modifier = Modifier.weight(1f)
                )
                BucketButton(
                    label = "~15/s",
                    sublabel = "elite",
                    isSelected = teleopData.shotRate == ShotRate.RATE_15,
                    onClick = { viewModel.setShotRate(ShotRate.RATE_15) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Accuracy")
            SegmentedButton(
                options = listOf("Bad", "OK", "Good", "Elite"),
                selectedOption = teleopData.accuracy?.name?.lowercase()?.replaceFirstChar { it.uppercase() },
                onOptionSelected = {
                    viewModel.setAccuracy(
                        when(it) {
                            "Bad" -> Accuracy.BAD
                            "OK" -> Accuracy.OK
                            "Good" -> Accuracy.GOOD
                            "Elite" -> Accuracy.ELITE
                            else -> null
                        }
                    )
                },
                columns = 4
            )
        }

        ScoutCard(title = "MOBILITY & INTAKE") {
            SectionLabel("Intake Speed")
            SegmentedButton(
                options = listOf("Slow", "OK", "Fast"),
                selectedOption = teleopData.intakeSpeed?.name?.lowercase()?.replaceFirstChar { it.uppercase() },
                onOptionSelected = {
                    viewModel.setIntakeSpeed(
                        when(it) {
                            "Slow" -> IntakeSpeed.SLOW
                            "OK" -> IntakeSpeed.OK
                            "Fast" -> IntakeSpeed.FAST
                            else -> null
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Crosses Bump?")
            SegmentedButton(
                options = listOf("No", "Sometimes", "Yes"),
                selectedOption = teleopData.crossesBump?.name?.lowercase()?.replaceFirstChar { it.uppercase() },
                onOptionSelected = {
                    viewModel.setBumpCrossing(
                        when(it) {
                            "No" -> BumpCrossing.NO
                            "Sometimes" -> BumpCrossing.SOMETIMES
                            "Yes" -> BumpCrossing.YES
                            else -> null
                        }
                    )
                }
            )
        }

        ScoutCard(title = "ROLE & STRATEGY") {
            SectionLabel("Role Tags (tap all that apply)")
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Primary Scorer",
                        isSelected = teleopData.roleTags.contains(RoleTags.PRIMARY_SCORER),
                        onClick = { viewModel.toggleRoleTag(RoleTags.PRIMARY_SCORER) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Support",
                        isSelected = teleopData.roleTags.contains(RoleTags.SUPPORT),
                        onClick = { viewModel.toggleRoleTag(RoleTags.SUPPORT) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Defense (others better)",
                        isSelected = teleopData.roleTags.contains(RoleTags.DEFENSE_OTHERS_BETTER),
                        onClick = { viewModel.toggleRoleTag(RoleTags.DEFENSE_OTHERS_BETTER) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Defense Specialist",
                        isSelected = teleopData.roleTags.contains(RoleTags.DEFENSE_SPECIALIST),
                        onClick = { viewModel.toggleRoleTag(RoleTags.DEFENSE_SPECIALIST) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Feeding",
                        isSelected = teleopData.roleTags.contains(RoleTags.FEEDING),
                        onClick = { viewModel.toggleRoleTag(RoleTags.FEEDING) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        ScoutCard(title = "QUICK EVENT TRACKER") {
            androidx.compose.material3.Text(
                text = "⚡ Eyes-free tracking - tap when you see it",
                style = MaterialTheme.typography.bodySmall,
                color = DarkAccent,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    text = "Good Play",
                    onClick = { viewModel.incrementCounter(ScoutingViewModel.CounterType.GOOD_PLAY) },
                    color = DarkGood,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Mistake",
                    onClick = { viewModel.incrementCounter(ScoutingViewModel.CounterType.MISTAKE) },
                    color = DarkBad,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    text = "Foul Risk",
                    onClick = { viewModel.incrementCounter(ScoutingViewModel.CounterType.FOUL_RISK) },
                    color = DarkWarn,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Clutch",
                    onClick = { viewModel.incrementCounter(ScoutingViewModel.CounterType.CLUTCH) },
                    color = DarkAccent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CounterDisplay("Good Plays", teleopData.goodPlays)
                CounterDisplay("Mistakes", teleopData.mistakes)
                CounterDisplay("Foul Risks", teleopData.foulRisks)
                CounterDisplay("Clutch Moments", teleopData.clutchMoments)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    text = "Undo Last",
                    onClick = { viewModel.undoLastCounter() },
                    color = DarkMuted,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Clear All",
                    onClick = { viewModel.clearCounters() },
                    color = DarkBad,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}


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
import com.owen.superalliance.ui.theme.DarkAccent
import com.owen.superalliance.ui.theme.DarkBackground

@Composable
fun PreMatchScreen(
    viewModel: ScoutingViewModel,
    onStartMatch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val matchData = viewModel.matchData

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Assignment Card
        ScoutCard(title = "MISSION ASSIGNMENT") {
            SectionLabel("Scout Station")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(Station.B1, Station.B2, Station.B3, Station.R1, Station.R2, Station.R3).forEach { station ->
                    TagButton(
                        label = station.name,
                        isSelected = matchData.station == station,
                        onClick = { viewModel.setStation(station) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Match #")
                    androidx.compose.material3.OutlinedTextField(
                        value = matchData.matchNumber?.toString() ?: "",
                        onValueChange = {
                            viewModel.setMatchNumber(it.toIntOrNull())
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { androidx.compose.material3.Text("12") }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Team #")
                    androidx.compose.material3.OutlinedTextField(
                        value = matchData.teamNumber?.toString() ?: "",
                        onValueChange = {
                            viewModel.setTeamNumber(it.toIntOrNull())
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { androidx.compose.material3.Text("254") }
                    )
                }
            }
        }

        // Pre-match predictions
        ScoutCard(title = "PRE-MATCH INTEL") {
            SectionLabel("Predicted Performance")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BucketButton(
                    label = "Low",
                    sublabel = "struggles",
                    isSelected = matchData.predictedPerformance == PerformanceBucket.LOW,
                    onClick = { viewModel.setPredictedPerformance(PerformanceBucket.LOW) },
                    modifier = Modifier.weight(1f)
                )
                BucketButton(
                    label = "Mid",
                    sublabel = "average",
                    isSelected = matchData.predictedPerformance == PerformanceBucket.MID,
                    onClick = { viewModel.setPredictedPerformance(PerformanceBucket.MID) },
                    modifier = Modifier.weight(1f)
                )
                BucketButton(
                    label = "High",
                    sublabel = "strong",
                    isSelected = matchData.predictedPerformance == PerformanceBucket.HIGH,
                    onClick = { viewModel.setPredictedPerformance(PerformanceBucket.HIGH) },
                    modifier = Modifier.weight(1f)
                )
                BucketButton(
                    label = "Elite",
                    sublabel = "top tier",
                    isSelected = matchData.predictedPerformance == PerformanceBucket.ELITE,
                    onClick = { viewModel.setPredictedPerformance(PerformanceBucket.ELITE) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Quick Tags (optional)")
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "New Bot",
                        isSelected = matchData.prematchTags.contains(PrematchTags.NEW_BOT),
                        onClick = { viewModel.togglePrematchTag(PrematchTags.NEW_BOT) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Still Tuning",
                        isSelected = matchData.prematchTags.contains(PrematchTags.STILL_TUNING),
                        onClick = { viewModel.togglePrematchTag(PrematchTags.STILL_TUNING) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Defense Bot",
                        isSelected = matchData.prematchTags.contains(PrematchTags.DEFENSE_LIKELY),
                        onClick = { viewModel.togglePrematchTag(PrematchTags.DEFENSE_LIKELY) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Shooter Bot",
                        isSelected = matchData.prematchTags.contains(PrematchTags.SHOOTER_BOT),
                        onClick = { viewModel.togglePrematchTag(PrematchTags.SHOOTER_BOT) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Climb Bot",
                        isSelected = matchData.prematchTags.contains(PrematchTags.CLIMB_BOT),
                        onClick = { viewModel.togglePrematchTag(PrematchTags.CLIMB_BOT) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Fast Cycles",
                        isSelected = matchData.prematchTags.contains(PrematchTags.FAST_CYCLES),
                        onClick = { viewModel.togglePrematchTag(PrematchTags.FAST_CYCLES) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Start button
        val canStart = matchData.station != null &&
                       matchData.matchNumber != null &&
                       matchData.teamNumber != null

        ActionButton(
            text = "BEGIN SCOUTING →",
            onClick = onStartMatch,
            modifier = Modifier.fillMaxWidth(),
            color = DarkAccent,
            enabled = canStart
        )

        if (!canStart) {
            androidx.compose.material3.Text(
                text = "⚠ Please set station, match, and team number",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Bottom nav spacing
    }
}


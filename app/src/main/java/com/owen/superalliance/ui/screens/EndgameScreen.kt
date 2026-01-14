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
fun EndgameScreen(
    viewModel: ScoutingViewModel,
    modifier: Modifier = Modifier
) {
    val endgameData = viewModel.matchData.endgameData

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScoutCard(title = "CLIMB PERFORMANCE") {
            SectionLabel("Climb Level Achieved")
            SegmentedButton(
                options = listOf("None", "L1", "L2", "L3"),
                selectedOption = endgameData.climbLevel.displayName,
                onOptionSelected = {
                    viewModel.setClimbLevel(
                        when(it) {
                            "L1" -> ClimbLevel.L1
                            "L2" -> ClimbLevel.L2
                            "L3" -> ClimbLevel.L3
                            else -> ClimbLevel.NONE
                        }
                    )
                },
                columns = 4
            )

            if (endgameData.climbLevel != ClimbLevel.NONE) {
                Spacer(modifier = Modifier.height(12.dp))

                SectionLabel("Climb Time (approximate)")
                SegmentedButton(
                    options = listOf("<5s", "5-10s", "10-15s", "15+s"),
                    selectedOption = endgameData.climbTime?.displayName,
                    onOptionSelected = {
                        viewModel.setClimbTime(
                            when(it) {
                                "<5s" -> ClimbTime.UNDER_5
                                "5-10s" -> ClimbTime.FIVE_TO_10
                                "10-15s" -> ClimbTime.TEN_TO_15
                                "15+s" -> ClimbTime.OVER_15
                                else -> null
                            }
                        )
                    },
                    columns = 4
                )

                Spacer(modifier = Modifier.height(8.dp))

                androidx.compose.material3.Text(
                    text = "⚡ Points: ${endgameData.climbLevel.points} | Fast climb = more time to score!",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkGood
                )
            }
        }

        ScoutCard(title = "ENDGAME STRATEGY") {
            SectionLabel("Behavior Tags (tap all that apply)")
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Leaves Late",
                        isSelected = endgameData.endgameTags.contains(EndgameTags.LEAVES_LATE),
                        onClick = { viewModel.toggleEndgameTag(EndgameTags.LEAVES_LATE) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Plays It Safe",
                        isSelected = endgameData.endgameTags.contains(EndgameTags.PLAYS_IT_SAFE),
                        onClick = { viewModel.toggleEndgameTag(EndgameTags.PLAYS_IT_SAFE) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Risky (works)",
                        isSelected = endgameData.endgameTags.contains(EndgameTags.RISKY_WORKS),
                        onClick = { viewModel.toggleEndgameTag(EndgameTags.RISKY_WORKS) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Risky (fails)",
                        isSelected = endgameData.endgameTags.contains(EndgameTags.RISKY_FAILS),
                        onClick = { viewModel.toggleEndgameTag(EndgameTags.RISKY_FAILS) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Shoots While Climbing",
                        isSelected = endgameData.endgameTags.contains(EndgameTags.SHOOTS_WHILE_CLIMBING),
                        onClick = { viewModel.toggleEndgameTag(EndgameTags.SHOOTS_WHILE_CLIMBING) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Assists Others",
                        isSelected = endgameData.endgameTags.contains(EndgameTags.ASSISTS_OTHERS),
                        onClick = { viewModel.toggleEndgameTag(EndgameTags.ASSISTS_OTHERS) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        ScoutCard(title = "MATCH SUMMARY") {
            SectionLabel("Overall Performance Tags")
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Reliable",
                        isSelected = endgameData.summaryTags.contains(SummaryTags.RELIABLE),
                        onClick = { viewModel.toggleSummaryTag(SummaryTags.RELIABLE) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Inconsistent",
                        isSelected = endgameData.summaryTags.contains(SummaryTags.INCONSISTENT),
                        onClick = { viewModel.toggleSummaryTag(SummaryTags.INCONSISTENT) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Great Driver",
                        isSelected = endgameData.summaryTags.contains(SummaryTags.GREAT_DRIVER),
                        onClick = { viewModel.toggleSummaryTag(SummaryTags.GREAT_DRIVER) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Tough Defense",
                        isSelected = endgameData.summaryTags.contains(SummaryTags.TOUGH_DEFENSE),
                        onClick = { viewModel.toggleSummaryTag(SummaryTags.TOUGH_DEFENSE) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagButton(
                        label = "Broke Down",
                        isSelected = endgameData.summaryTags.contains(SummaryTags.BROKE_DOWN),
                        onClick = { viewModel.toggleSummaryTag(SummaryTags.BROKE_DOWN) },
                        modifier = Modifier.weight(1f)
                    )
                    TagButton(
                        label = "Comm Issues",
                        isSelected = endgameData.summaryTags.contains(SummaryTags.COMM_ISSUES),
                        onClick = { viewModel.toggleSummaryTag(SummaryTags.COMM_ISSUES) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            SectionLabel("Final Note (optional - one sentence max)")
            androidx.compose.material3.OutlinedTextField(
                value = endgameData.finalNote,
                onValueChange = { viewModel.setFinalNote(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                placeholder = {
                    androidx.compose.material3.Text("Any critical observations...")
                },
                maxLines = 3
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}


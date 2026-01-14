package com.owen.superalliance.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.owen.superalliance.*
import com.owen.superalliance.ui.components.*
import com.owen.superalliance.ui.theme.*

@Composable
fun ExportScreen(
    viewModel: ScoutingViewModel,
    modifier: Modifier = Modifier
) {
    val matchData = viewModel.matchData
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScoutCard(title = "MISSION REVIEW") {
            CounterDisplay("Station", matchData.station?.name ?: "—")
            Spacer(modifier = Modifier.height(8.dp))
            CounterDisplay("Match", matchData.matchNumber?.toString() ?: "—")
            Spacer(modifier = Modifier.height(8.dp))
            CounterDisplay("Team", matchData.teamNumber?.toString() ?: "—")

            Spacer(modifier = Modifier.height(16.dp))

            // Quick summary
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                androidx.compose.material3.Text(
                    text = "Auto: ${matchData.autoData.startPosition?.name ?: "Not set"} start • ${matchData.autoData.autoSpeed?.name ?: "Unknown"} speed",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkMuted
                )

                androidx.compose.material3.Text(
                    text = "Teleop: ${matchData.teleopData.shotRate?.displayName ?: "Not rated"} scoring • ${matchData.teleopData.accuracy?.name ?: "Unknown"} accuracy",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkMuted
                )

                androidx.compose.material3.Text(
                    text = "Endgame: ${matchData.endgameData.climbLevel.displayName} climb (${matchData.endgameData.climbLevel.points} pts)${
                        if (matchData.endgameData.climbTime != null) " in ${matchData.endgameData.climbTime.displayName}" else ""
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (matchData.endgameData.climbLevel != ClimbLevel.NONE) DarkGood else DarkMuted
                )

                if (matchData.teleopData.goodPlays > 0 || matchData.teleopData.mistakes > 0 ||
                    matchData.teleopData.clutchMoments > 0 || matchData.teleopData.foulRisks > 0) {
                    androidx.compose.material3.Text(
                        text = "Events: ${matchData.teleopData.goodPlays} good • ${matchData.teleopData.mistakes} mistakes • ${matchData.teleopData.clutchMoments} clutch • ${matchData.teleopData.foulRisks} fouls",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkMuted
                    )
                }
            }
        }

        ScoutCard(title = "DATA EXPORT") {
            androidx.compose.material3.Text(
                text = "JSON Format (for database submission)",
                style = MaterialTheme.typography.bodySmall,
                color = DarkAccent,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            androidx.compose.material3.OutlinedTextField(
                value = viewModel.toJson(),
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                readOnly = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace
                ),
                maxLines = 12
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    text = "Copy JSON",
                    onClick = {
                        copyToClipboard(context, viewModel.toJson())
                        Toast.makeText(context, "✓ Copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    color = DarkAccent,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Submit",
                    onClick = {
                        // TODO: Implement MongoDB/TBA submission
                        Toast.makeText(context, "Database sync coming soon!", Toast.LENGTH_SHORT).show()
                    },
                    color = DarkGood,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.material3.Text(
                text = "💾 Future: Auto-sync to MongoDB Atlas",
                style = MaterialTheme.typography.bodySmall,
                color = DarkMuted
            )
        }

        ScoutCard(title = "NEXT MATCH") {
            ActionButton(
                text = "🔄 RESET ALL & START NEW SCOUT",
                onClick = {
                    viewModel.resetAll()
                    Toast.makeText(context, "Ready for next match", Toast.LENGTH_SHORT).show()
                },
                color = DarkBad,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.material3.Text(
                text = "⚠ This will clear all data. Make sure you've exported first!",
                style = MaterialTheme.typography.bodySmall,
                color = DarkWarn
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Scouting Data", text)
    clipboard.setPrimaryClip(clip)
}


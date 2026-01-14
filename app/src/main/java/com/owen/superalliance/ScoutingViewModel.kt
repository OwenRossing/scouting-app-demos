package com.owen.superalliance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel for managing scouting match state
 */
class ScoutingViewModel : ViewModel() {
    var matchData by mutableStateOf(ScoutingMatch())
        private set

    var currentPhase by mutableStateOf(Phase.PRE)
        private set

    // Timer state
    var timerRunning by mutableStateOf(false)
        private set

    var remainingSeconds by mutableStateOf(150) // 2:30 default
        private set

    // Undo stack for counters
    private val undoStack = mutableListOf<CounterAction>()

    // Phase management
    enum class Phase {
        PRE, AUTO, TELEOP, ENDGAME, EXPORT
    }

    private data class CounterAction(
        val type: CounterType,
        val timestamp: Long = System.currentTimeMillis()
    )

    enum class CounterType {
        GOOD_PLAY, MISTAKE, FOUL_RISK, CLUTCH
    }

    fun setPhase(phase: Phase) {
        currentPhase = phase
    }

    // Assignment
    fun setStation(station: Station) {
        matchData = matchData.copy(station = station)
    }

    fun setMatchNumber(num: Int?) {
        matchData = matchData.copy(matchNumber = num)
    }

    fun setTeamNumber(num: Int?) {
        matchData = matchData.copy(teamNumber = num)
    }

    // Pre-match
    fun setPredictedPerformance(bucket: PerformanceBucket?) {
        matchData = matchData.copy(predictedPerformance = bucket)
    }

    fun togglePrematchTag(tag: String) {
        val tags = matchData.prematchTags.toMutableSet()
        if (tags.contains(tag)) {
            tags.remove(tag)
        } else {
            tags.add(tag)
        }
        matchData = matchData.copy(prematchTags = tags)
    }

    // Auto
    fun setStartPosition(pos: StartPosition?) {
        matchData = matchData.copy(
            autoData = matchData.autoData.copy(startPosition = pos)
        )
    }

    fun setAutoSpeed(speed: AutoSpeed?) {
        matchData = matchData.copy(
            autoData = matchData.autoData.copy(autoSpeed = speed)
        )
    }

    fun toggleAutoCrossesBump() {
        matchData = matchData.copy(
            autoData = matchData.autoData.copy(
                crossesBump = !matchData.autoData.crossesBump
            )
        )
    }

    fun toggleAutoIntakes() {
        matchData = matchData.copy(
            autoData = matchData.autoData.copy(
                intakesInAuto = !matchData.autoData.intakesInAuto
            )
        )
    }

    fun toggleAutoShoots() {
        matchData = matchData.copy(
            autoData = matchData.autoData.copy(
                shootsInAuto = !matchData.autoData.shootsInAuto
            )
        )
    }

    fun toggleAutoClimbs() {
        matchData = matchData.copy(
            autoData = matchData.autoData.copy(
                climbsInAuto = !matchData.autoData.climbsInAuto
            )
        )
    }

    fun setAutoClimbLevel(level: AutoClimbLevel) {
        matchData = matchData.copy(
            autoData = matchData.autoData.copy(autoClimbLevel = level)
        )
    }

    fun setAutoDescription(desc: String) {
        matchData = matchData.copy(
            autoData = matchData.autoData.copy(description = desc)
        )
    }

    fun clearAutoData() {
        matchData = matchData.copy(autoData = AutoData())
    }

    // Teleop
    fun setShotRate(rate: ShotRate?) {
        matchData = matchData.copy(
            teleopData = matchData.teleopData.copy(shotRate = rate)
        )
    }

    fun setAccuracy(acc: Accuracy?) {
        matchData = matchData.copy(
            teleopData = matchData.teleopData.copy(accuracy = acc)
        )
    }

    fun setIntakeSpeed(speed: IntakeSpeed?) {
        matchData = matchData.copy(
            teleopData = matchData.teleopData.copy(intakeSpeed = speed)
        )
    }

    fun setBumpCrossing(crossing: BumpCrossing?) {
        matchData = matchData.copy(
            teleopData = matchData.teleopData.copy(crossesBump = crossing)
        )
    }

    fun toggleRoleTag(tag: String) {
        val tags = matchData.teleopData.roleTags.toMutableSet()
        if (tags.contains(tag)) {
            tags.remove(tag)
        } else {
            tags.add(tag)
        }
        matchData = matchData.copy(
            teleopData = matchData.teleopData.copy(roleTags = tags)
        )
    }

    fun incrementCounter(type: CounterType) {
        undoStack.add(CounterAction(type))

        val updated = when (type) {
            CounterType.GOOD_PLAY -> matchData.teleopData.copy(
                goodPlays = matchData.teleopData.goodPlays + 1
            )
            CounterType.MISTAKE -> matchData.teleopData.copy(
                mistakes = matchData.teleopData.mistakes + 1
            )
            CounterType.FOUL_RISK -> matchData.teleopData.copy(
                foulRisks = matchData.teleopData.foulRisks + 1
            )
            CounterType.CLUTCH -> matchData.teleopData.copy(
                clutchMoments = matchData.teleopData.clutchMoments + 1
            )
        }

        matchData = matchData.copy(teleopData = updated)
    }

    fun undoLastCounter() {
        if (undoStack.isEmpty()) return

        val last = undoStack.removeLast()

        val updated = when (last.type) {
            CounterType.GOOD_PLAY -> matchData.teleopData.copy(
                goodPlays = maxOf(0, matchData.teleopData.goodPlays - 1)
            )
            CounterType.MISTAKE -> matchData.teleopData.copy(
                mistakes = maxOf(0, matchData.teleopData.mistakes - 1)
            )
            CounterType.FOUL_RISK -> matchData.teleopData.copy(
                foulRisks = maxOf(0, matchData.teleopData.foulRisks - 1)
            )
            CounterType.CLUTCH -> matchData.teleopData.copy(
                clutchMoments = maxOf(0, matchData.teleopData.clutchMoments - 1)
            )
        }

        matchData = matchData.copy(teleopData = updated)
    }

    fun clearCounters() {
        undoStack.clear()
        matchData = matchData.copy(
            teleopData = matchData.teleopData.copy(
                goodPlays = 0,
                mistakes = 0,
                foulRisks = 0,
                clutchMoments = 0
            )
        )
    }

    // Endgame
    fun setClimbLevel(level: ClimbLevel) {
        matchData = matchData.copy(
            endgameData = matchData.endgameData.copy(climbLevel = level)
        )
    }

    fun setClimbTime(time: ClimbTime?) {
        matchData = matchData.copy(
            endgameData = matchData.endgameData.copy(climbTime = time)
        )
    }

    fun toggleEndgameTag(tag: String) {
        val tags = matchData.endgameData.endgameTags.toMutableSet()
        if (tags.contains(tag)) {
            tags.remove(tag)
        } else {
            tags.add(tag)
        }
        matchData = matchData.copy(
            endgameData = matchData.endgameData.copy(endgameTags = tags)
        )
    }

    fun toggleSummaryTag(tag: String) {
        val tags = matchData.endgameData.summaryTags.toMutableSet()
        if (tags.contains(tag)) {
            tags.remove(tag)
        } else {
            tags.add(tag)
        }
        matchData = matchData.copy(
            endgameData = matchData.endgameData.copy(summaryTags = tags)
        )
    }

    fun setFinalNote(note: String) {
        matchData = matchData.copy(
            endgameData = matchData.endgameData.copy(finalNote = note)
        )
    }

    // Complete reset
    fun resetAll() {
        matchData = ScoutingMatch()
        currentPhase = Phase.PRE
        timerRunning = false
        remainingSeconds = 150
        undoStack.clear()
    }

    // Timer
    fun startTimer() {
        timerRunning = true
    }

    fun pauseTimer() {
        timerRunning = false
    }

    fun resetTimer() {
        timerRunning = false
        remainingSeconds = 150
    }

    fun tick() {
        if (timerRunning && remainingSeconds > 0) {
            remainingSeconds--
        }
    }

    // Export to JSON
    fun toJson(): String {
        return """
{
  "station": "${matchData.station?.name ?: "null"}",
  "matchNumber": ${matchData.matchNumber ?: "null"},
  "teamNumber": ${matchData.teamNumber ?: "null"},
  "timestamp": ${matchData.timestamp},
  "prematch": {
    "predictedPerformance": "${matchData.predictedPerformance?.name ?: "null"}",
    "tags": [${matchData.prematchTags.joinToString { "\"$it\"" }}]
  },
  "auto": {
    "startPosition": "${matchData.autoData.startPosition?.name ?: "null"}",
    "speed": "${matchData.autoData.autoSpeed?.name ?: "null"}",
    "crossesBump": ${matchData.autoData.crossesBump},
    "intakesInAuto": ${matchData.autoData.intakesInAuto},
    "shootsInAuto": ${matchData.autoData.shootsInAuto},
    "climbsInAuto": ${matchData.autoData.climbsInAuto},
    "climbLevel": "${matchData.autoData.autoClimbLevel.displayName}",
    "description": "${matchData.autoData.description.replace("\"", "\\\"")}"
  },
  "teleop": {
    "shotRate": "${matchData.teleopData.shotRate?.displayName ?: "null"}",
    "accuracy": "${matchData.teleopData.accuracy?.name ?: "null"}",
    "intakeSpeed": "${matchData.teleopData.intakeSpeed?.name ?: "null"}",
    "crossesBump": "${matchData.teleopData.crossesBump?.name ?: "null"}",
    "roleTags": [${matchData.teleopData.roleTags.joinToString { "\"$it\"" }}],
    "goodPlays": ${matchData.teleopData.goodPlays},
    "mistakes": ${matchData.teleopData.mistakes},
    "foulRisks": ${matchData.teleopData.foulRisks},
    "clutchMoments": ${matchData.teleopData.clutchMoments}
  },
  "endgame": {
    "climbLevel": "${matchData.endgameData.climbLevel.displayName}",
    "climbTime": "${matchData.endgameData.climbTime?.displayName ?: "null"}",
    "tags": [${matchData.endgameData.endgameTags.joinToString { "\"$it\"" }}],
    "summaryTags": [${matchData.endgameData.summaryTags.joinToString { "\"$it\"" }}],
    "finalNote": "${matchData.endgameData.finalNote.replace("\"", "\\\"")}"
  }
}
        """.trimIndent()
    }
}


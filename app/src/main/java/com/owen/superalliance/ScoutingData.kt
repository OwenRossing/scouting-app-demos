package com.owen.superalliance

import androidx.compose.runtime.Immutable

/**
 * Data model for FRC 2026 scouting
 */

@Immutable
data class ScoutingMatch(
    // Assignment
    val station: Station? = null,
    val matchNumber: Int? = null,
    val teamNumber: Int? = null,

    // Pre-match
    val predictedPerformance: PerformanceBucket? = null,
    val prematchTags: Set<String> = emptySet(),

    // Auto
    val autoData: AutoData = AutoData(),

    // Teleop
    val teleopData: TeleopData = TeleopData(),

    // Endgame
    val endgameData: EndgameData = EndgameData(),

    // Timing
    val timestamp: Long = System.currentTimeMillis()
)

enum class Station {
    B1, B2, B3, R1, R2, R3
}

enum class PerformanceBucket {
    LOW, MID, HIGH, ELITE, UNKNOWN
}

@Immutable
data class AutoData(
    val startPosition: StartPosition? = null,
    val autoSpeed: AutoSpeed? = null,
    val crossesBump: Boolean = false,
    val intakesInAuto: Boolean = false,
    val shootsInAuto: Boolean = false,
    val climbsInAuto: Boolean = false,
    val autoClimbLevel: AutoClimbLevel = AutoClimbLevel.NONE,
    val description: String = ""
)

enum class StartPosition {
    LEFT, CENTER, RIGHT
}

enum class AutoSpeed {
    SLOW, NORMAL, FAST
}

// Only NONE and L1 are possible in auto (L2/L3 are impossible)
enum class AutoClimbLevel(val displayName: String) {
    NONE("None"),
    L1("L1")
}

@Immutable
data class TeleopData(
    val shotRate: ShotRate? = null,
    val accuracy: Accuracy? = null,
    val intakeSpeed: IntakeSpeed? = null,
    val crossesBump: BumpCrossing? = null,
    val roleTags: Set<String> = emptySet(),
    val goodPlays: Int = 0,
    val mistakes: Int = 0,
    val foulRisks: Int = 0,
    val clutchMoments: Int = 0
)

enum class ShotRate(val displayName: String) {
    RATE_1("~1/s"),
    RATE_5("~5/s"),
    RATE_10("~10/s"),
    RATE_15("~15/s"),
    VARIES("Varies")
}

enum class Accuracy {
    BAD, OK, GOOD, ELITE
}

enum class IntakeSpeed {
    SLOW, OK, FAST
}

enum class BumpCrossing {
    NO, SOMETIMES, YES
}

@Immutable
data class EndgameData(
    val climbLevel: ClimbLevel = ClimbLevel.NONE,
    val climbTime: ClimbTime? = null,
    val endgameTags: Set<String> = emptySet(),
    val summaryTags: Set<String> = emptySet(),
    val finalNote: String = ""
)

enum class ClimbLevel(val displayName: String, val points: Int) {
    NONE("None", 0),
    L1("L1", 3),
    L2("L2", 6),
    L3("L3", 12)
}

enum class ClimbTime(val displayName: String) {
    UNDER_5("<5s"),
    FIVE_TO_10("5-10s"),
    TEN_TO_15("10-15s"),
    OVER_15("15+s")
}

// Pre-defined tags to avoid typos
object PrematchTags {
    const val NEW_BOT = "NewBot"
    const val STILL_TUNING = "StillTuning"
    const val DEFENSE_LIKELY = "DefenseLikely"
    const val SHOOTER_BOT = "ShooterBot"
    const val CLIMB_BOT = "ClimbBot"
    const val FAST_CYCLES = "FastCycles"
}

object RoleTags {
    const val PRIMARY_SCORER = "PrimaryScorer"
    const val SUPPORT = "Support"
    const val DEFENSE_OTHERS_BETTER = "DefenseBecauseOthersBetter"
    const val DEFENSE_SPECIALIST = "DefenseSpecialist"
    const val FEEDING = "Feeding"
    const val AVOIDS_CONTACT = "AvoidsContact"
}

object EndgameTags {
    const val LEAVES_LATE = "LeavesLate"
    const val PLAYS_IT_SAFE = "PlaysItSafe"
    const val RISKY_WORKS = "RiskyButWorks"
    const val RISKY_FAILS = "RiskyFails"
    const val SHOOTS_WHILE_CLIMBING = "ShootsWhileClimbing"
    const val ASSISTS_OTHERS = "AssistsOthers"
}

object SummaryTags {
    const val RELIABLE = "Reliable"
    const val INCONSISTENT = "Inconsistent"
    const val GREAT_DRIVER = "GreatDriver"
    const val TOUGH_DEFENSE = "BadDefenseToFace"
    const val BROKE_DOWN = "BrokeDown"
    const val COMM_ISSUES = "CommIssues"
}


package com.midina.stat_data.teamsStatistcsData

data class Response(
    val biggest: Biggest,
    val cards: Cards,
    val clean_sheet: CleanSheet,
    val failed_to_score: FailedToScore,
    val fixtures: Fixtures,
    val form: String,
    val goals: GoalsX,
    val league: League,
    val lineups: List<Lineup>,
    val penalty: Penalty,
    val team: Team
)
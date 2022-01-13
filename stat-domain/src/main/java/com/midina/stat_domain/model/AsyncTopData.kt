package com.midina.stat_domain.model

sealed class AsyncTopData {
    class AsyncSeason(val season: Int) : AsyncTopData()
    class AsyncTopScorer(val player: TopScorer) : AsyncTopData()
    class AsyncTopAssistant(val player: TopAssistant) : AsyncTopData()
    class AsyncTopTeams(val teams: Pair<TopCleanSheet?, TopTeamGoals?>) : AsyncTopData()
}

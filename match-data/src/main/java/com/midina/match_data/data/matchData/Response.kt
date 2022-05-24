package com.midina.match_data.data.matchData

data class Response(
    val events: List<Event>,
    val fixture: Fixture,
    val goals: Goals,
    val league: League,
    val lineups: List<Lineup>,
    val players: List<PlayerXXX>,
    val score: Score,
    val statistics: List<StatisticX>,
    val teams: Teams
)
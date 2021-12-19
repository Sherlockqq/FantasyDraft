package com.midina.stat_data.data

data class LeaguePreferences(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)
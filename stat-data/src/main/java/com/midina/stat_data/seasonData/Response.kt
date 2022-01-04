package com.midina.stat_data.seasonData

data class Response(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)
package com.midina.matches_data.data.league

data class Response(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)
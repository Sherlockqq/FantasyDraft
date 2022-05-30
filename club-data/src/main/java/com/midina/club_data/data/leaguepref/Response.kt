package com.midina.club_data.data.leaguepref

data class Response(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)
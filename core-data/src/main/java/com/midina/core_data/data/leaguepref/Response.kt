package com.midina.core_data.data.leaguepref

data class Response(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)
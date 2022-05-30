package com.midina.club_data.data.leaguepref

data class LeaguePreferences(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)
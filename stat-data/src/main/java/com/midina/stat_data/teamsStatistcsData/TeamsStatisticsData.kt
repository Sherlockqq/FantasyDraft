package com.midina.stat_data.teamsStatistcsData

data class TeamsStatisticsData(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: Response,
    val results: Int
)
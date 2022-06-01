package com.midina.club_data.data.teaminfodata

data class TeamInfoData(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)
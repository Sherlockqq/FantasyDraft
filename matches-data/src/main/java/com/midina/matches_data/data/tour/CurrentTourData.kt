package com.midina.matches_data.data.tour

data class CurrentTourData(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<String>,
    val results: Int
)
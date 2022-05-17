package com.midina.matches_data.data.fixtures

data class Fixture(
    val date: String,
    val id: Int,
    val periods: Periods,
    val referee: String,
    val status: Status,
    val timestamp: Int,
    val timezone: String,
    val venue: Venue
)
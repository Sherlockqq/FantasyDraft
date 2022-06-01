package com.midina.match_data.data.matchData

data class Lineup(
    val coach: Coach,
    val formation: String,
    val startXI: List<StartXI>,
    val substitutes: List<Substitute>,
    val team: TeamX
)
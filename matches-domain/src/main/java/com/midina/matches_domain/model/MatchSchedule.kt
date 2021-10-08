package com.midina.matches_domain.model

data class MatchSchedule(
    var id : Int,
    var tour : Int,
    var homeTeam : String,
    var guestTeam : String,
    var date : String,
    var score : String,
    val isHeader : Boolean? = null
)

enum class Weather {

}
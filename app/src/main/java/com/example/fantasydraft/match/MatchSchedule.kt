package com.example.fantasydraft.match

data class MatchSchedule(
    var id : Int,
    var tour : Int,
    var homeTeam : String,
    var guestTeam : String,
    var date : String,
    var score : String
)


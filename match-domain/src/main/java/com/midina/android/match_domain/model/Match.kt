package com.midina.android.match_domain.model

data class Match(
    val fixture: Fixture,
    val teams: Teams,
    val goals: Goals
)
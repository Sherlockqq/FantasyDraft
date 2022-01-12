package com.midina.stat_domain.model

data class TopData(
    val scorer: ResultEvent<TopScorer>,
    val assistant: ResultEvent<TopAssistant>,
    val teamStat: ResultEvent<Pair<TopCleanSheet?, TopTeamGoals?>>
)
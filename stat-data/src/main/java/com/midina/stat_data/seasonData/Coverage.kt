package com.midina.stat_data.seasonData

data class Coverage(
    val fixtures: Fixtures,
    val injuries: Boolean,
    val odds: Boolean,
    val players: Boolean,
    val predictions: Boolean,
    val standings: Boolean,
    val top_assists: Boolean,
    val top_cards: Boolean,
    val top_scorers: Boolean
)
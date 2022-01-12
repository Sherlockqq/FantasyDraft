package com.midina.stat_data.topPlayersData

data class Response(
    val player: Player,
    val statistics: List<Statistic>
)
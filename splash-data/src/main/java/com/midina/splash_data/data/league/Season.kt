package com.midina.splash_data.data.league

data class Season(
    val coverage: Coverage,
    val current: Boolean,
    val end: String,
    val start: String,
    val year: Int
)
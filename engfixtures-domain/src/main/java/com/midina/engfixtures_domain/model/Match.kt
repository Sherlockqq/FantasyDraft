package com.midina.engfixtures_domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Match(
    val id: Int,
    val homeTeamId: Int,
    val homeTeamName: String,
    val homeTeamLogo: String,
    val homeTeamGoals: Int,
    val awayTeamId: Int,
    val awayTeamName: String,
    val awayTeamLogo: String,
    val awayTeamGoals: Int,
    val status: String,
    val date: String,
    val tour: Int
) : Parcelable
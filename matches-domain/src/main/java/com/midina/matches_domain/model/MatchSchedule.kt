package com.midina.matches_domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchSchedule(
    val fixtureId: Int,
    val tour: Int,
    val homeTeam: String,
    val homeLogo: String,
    val guestTeam: String,
    val guestLogo: String,
    val date: String,
    val score: String,
    val status: String
) : Parcelable
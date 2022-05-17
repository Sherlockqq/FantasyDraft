package com.midina.matches_domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchSchedule(
    var tour: Int,
    var homeTeam: String,
    var homeLogo: String,
    var guestTeam: String,
    var guestLogo: String,
    var date: String,
    var score: String
) : Parcelable
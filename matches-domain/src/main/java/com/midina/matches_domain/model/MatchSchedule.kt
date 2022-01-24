package com.midina.matches_domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchSchedule(
    var id: Int,
    var tour: Int,
    var homeTeam: String,
    var guestTeam: String,
    var date: String,
    var score: String,
    val isHeader: Boolean? = null
) : Parcelable
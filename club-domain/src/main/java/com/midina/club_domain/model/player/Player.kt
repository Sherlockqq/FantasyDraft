package com.midina.club_domain.model.player

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val id: Int,
    val name: String,
    val nationality: String,
    val position: String,
    val photo: String,
    val isHeader: Boolean
): Parcelable

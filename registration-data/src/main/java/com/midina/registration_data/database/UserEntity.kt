package com.midina.registration_data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.midina.registration_domain.model.Gender
import java.util.*

@Entity
data class UserEntity(
    @PrimaryKey val emailAddress: String,
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    // val dateOfBirth : Date
)

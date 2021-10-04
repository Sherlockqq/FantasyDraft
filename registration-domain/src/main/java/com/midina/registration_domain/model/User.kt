package com.midina.registration_domain.model

import java.util.*

enum class Gender{
    MALE,
    FEMALE,
    UNSPECIFIED
}

data class User(
    val firstName : String,
    val lastName : String,
    val emailAddress : String,
    val gender : Gender,
    //val dateOfBirth : Date
)

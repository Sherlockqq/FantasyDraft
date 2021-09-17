package com.example.fantasydraft.registration

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
    val password : String,
    val gender : Gender,
    val dateOfBirth : Date
)

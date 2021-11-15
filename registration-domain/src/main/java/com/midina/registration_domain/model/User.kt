package com.midina.registration_domain.model

enum class Gender {
    MALE,
    FEMALE,
    UNSPECIFIED
}

data class User(
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val gender: Gender,
    val dateOfBirth: String
)

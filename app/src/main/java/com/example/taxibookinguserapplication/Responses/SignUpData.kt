package com.example.taxibookinguserapplication.Responses

data class SignUpData(
    val email: String,
    val id: Int,
    val language: String,
    val otp: Int,
    val phone: String
)
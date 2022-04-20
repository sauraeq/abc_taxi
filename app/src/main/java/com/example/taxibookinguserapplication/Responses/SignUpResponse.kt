package com.example.taxibookinguserapplication.Responses

data class SignUpResponse(
    val `data`: SignUpData,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
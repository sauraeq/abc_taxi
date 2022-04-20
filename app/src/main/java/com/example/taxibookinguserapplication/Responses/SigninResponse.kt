package com.example.taxibookinguserapplication.Responses

data class SigninResponse(
    val `data`: List<DataXX>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
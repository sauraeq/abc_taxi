package com.example.taxibookinguserapplication.Responses

data class LoginotpverifyResponse(
    val `data`: List<DataXXX>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
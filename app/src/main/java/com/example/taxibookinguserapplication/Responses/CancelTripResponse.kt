package com.example.taxibookinguserapplication.Responses

data class CancelTripResponse(
    val `data`: Boolean,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
package com.example.taxibookinguserapplication.Responses

data class CancelRideResonResponse(
    val `data`: List<CancelRideReasonResponseData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
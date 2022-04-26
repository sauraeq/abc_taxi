package com.example.taxibookinguserapplication.Responses

data class BookingStatusResponse(
    val `data`: List<BookingStausResponseData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
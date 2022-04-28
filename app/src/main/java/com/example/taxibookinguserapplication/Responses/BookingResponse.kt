package com.example.taxibookinguserapplication.Responses

data class BookingResponse(
    val `data`: List<BookingResponseData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
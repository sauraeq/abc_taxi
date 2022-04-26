package com.example.taxibookinguserapplication.Responses

data class TripHistory_Response(
    val `data`: List<TripHistoryData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
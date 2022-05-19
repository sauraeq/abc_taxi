package com.example.taxibookinguserapplication.Responses

data class RatingReviewResponse(
    val `data`: List<Any>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
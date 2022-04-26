package com.example.taxibookinguserapplication.Responses

data class EditImgResponse(
    val `data`: List<Any>,
    val error: Int,
    val image: String,
    val msg: String,
    val service: String,
    val success: String
)
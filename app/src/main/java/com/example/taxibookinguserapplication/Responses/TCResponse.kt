package com.example.taxibookinguserapplication.Responses

data class TCResponse(
    val `data`: List<TCResData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
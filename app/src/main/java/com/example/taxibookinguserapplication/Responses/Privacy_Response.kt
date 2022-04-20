package com.example.taxibookinguserapplication.Responses

data class Privacy_Response(
    val `data`: List<PrivacyResData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
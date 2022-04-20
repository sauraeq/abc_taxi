package com.example.taxibookinguserapplication.Responses

data class LanguageRespnse(
    val `data`: List<DataX>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
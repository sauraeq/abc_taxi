package com.example.taxibookinguserapplication.Responses

data class EditProfileResponse(
    val `data`: EditProfileResponseData,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
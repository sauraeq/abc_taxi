package com.example.taxibookinguserapplication.Responses

data class PaymentResponse(
    val `data`: PayemntResponseData,
    val error: String,
    val msg: String,
    val previous_expirydate: String,
    val service: String,
    val success: String
)
package com.example.taxibookinguserapplication.Responses

data class VehicleListResponse(
    val `data`: List<VehicleResponseData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)
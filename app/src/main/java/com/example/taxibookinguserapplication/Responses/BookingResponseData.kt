package com.example.taxibookinguserapplication.Responses

data class BookingResponseData(
    val booking_id: Int,
    val device_tokanid: String,
    val id: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val otp: Int,
    val profile_photo: String,
    val rating: String,
    val vehicle_image: String,
    val vehicle_name: String,
    val vehicle_no: String
)
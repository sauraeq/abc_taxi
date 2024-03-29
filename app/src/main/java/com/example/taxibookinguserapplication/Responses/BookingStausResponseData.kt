package com.example.taxibookinguserapplication.Responses

data class BookingStausResponseData(
    val booking_id: String,
    val device_tokanid: String,
    val id: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val otp: String,
    val profile_photo: String,
    val rating: String,
    val amount:String,
    val time:String,
    val distance:String,
    val status:String,
    val cancel:String,
     val otp_status:String,
    val vehicle_image: String,
    val vehicle_name: String,
    val mobile:String,
    val vehicle_no: String
)
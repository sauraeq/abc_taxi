package com.example.taxibookinguserapplication.Responses

data class TripHistoryData(
    val amount: Any,
    val cancel: String,
    val cancel_description: Any,
    val created_date: String,
    val cupon_code: String,
    val distance: String,
    val driver_id: String,
    val driver_name: String,
    val driver_photo: String,
    val drop_address: String,
    val drop_latitude: String,
    val drop_longitude: String,
    val id: String,
    val otp: Any,
    val rating:String,
    val pickup_address: String,
    val pickup_latitude: String,
    val pickup_longitude: String,
    val rejected_trip: Any,
    val status: String,
    val time: Any,
    val uid: String
)
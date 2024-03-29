package com.example.taxibookinguserapplication.util

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import android.widget.Toast
import java.lang.Exception
import java.util.*

class FetchAddressServices : IntentService("FetchAddressIntentServices") {
    var resultReceiver: ResultReceiver? = null
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            var errormessgae = ""
            resultReceiver = intent.getParcelableExtra(Constant.RECEVIER)
            val location = intent.getParcelableExtra<Location>(Constant.LOCATION_DATA_EXTRA)
                ?: return
            val geocoder = Geocoder(this, Locale.getDefault())
            var addresses: List<Address>? = null
            try {
                addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
            } catch (ioException: Exception) {
                Log.e("", "Error in getting address for the location")
            }
            if (addresses == null || addresses.size == 0) {
                errormessgae = "No address found for the location"
                Toast.makeText(this, "" + errormessgae, Toast.LENGTH_SHORT).show()
            } else {
                val address = addresses[0]
                val str_postcode = address.postalCode
                val str_Country = address.countryName
                val str_state = address.adminArea
                val str_district = address.subAdminArea
                val str_locality = address.locality
                val str_address = address.featureName
                devliverResultToRecevier(
                    Constant.SUCCESS_RESULT,
                    str_address,
                    str_locality,
                    str_district,
                    str_state,
                    str_Country,
                    str_postcode
                )
            }
        }
    }

    private fun devliverResultToRecevier(
        resultcode: Int,
        address: String,
        locality: String,
        district: String,
        state: String,
        country: String,
        postcode: String
    ) {
        val bundle = Bundle()
        bundle.putString(Constant.ADDRESS, address)
        bundle.putString(Constant.LOCAITY, locality)
        bundle.putString(Constant.DISTRICT, district)
        bundle.putString(Constant.STATE, state)
        bundle.putString(Constant.COUNTRY, country)
        bundle.putString(Constant.POST_CODE, postcode)
        resultReceiver!!.send(resultcode, bundle)
    }
}
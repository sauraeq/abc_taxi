package com.example.taxibookinguserapplication.LocationMap.Fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.util.Constant
import com.example.taxibookinguserapplication.util.FetchAddressServices

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class CurrentLocationFragment : Fragment() {
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2
    var locate: String = ""
    var latitudee: String = ""
    var longitudee: String = ""
    var LOCATION_PERMISSION_REQUEST_CODE = -1

    var resultReceiver: ResultReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_location, container, false)

        resultReceiver = AddressResultReceiver(Handler())
       /* if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {

        }*/
        currentLocation
          return view
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentLocation
            } else {
                Toast.makeText(requireContext(), "Permission is denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val currentLocation: Unit
        private get() {

            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }

            LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(requireContext())
                            .removeLocationUpdates(this)
                        if (locationResult.locations != null) {
                            if (locationResult.locations.size > 0) {
                                val latestlocIndex = locationResult.locations.size - 1
                                var lati = locationResult.locations[latestlocIndex].latitude
                                var longi = locationResult.locations[latestlocIndex].longitude
                                latitudee = lati.toString()
                                longitudee = longi.toString()

                                val location = Location("providerNA")
                                location.longitude = longi
                                location.latitude = lati
                                fetchaddressfromlocation(location)
                            } else {

                            }
                        }
                    }
                }, Looper.getMainLooper())
        }


    private inner class AddressResultReceiver(handler: Handler?) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)
            if (resultCode == Constant.SUCCESS_RESULT) {
                var address: String? = resultData.getString(Constant.ADDRESS)
                var locaity: String? = resultData.getString(Constant.LOCAITY)
                var state: String? = resultData.getString(Constant.STATE)
                var district: String? = resultData.getString(Constant.DISTRICT)
                var country: String? = resultData.getString(Constant.ADDRESS)
                locate = address + "," + locaity + "," + state
                loadmap(locate, latitudee, longitudee)


            } else {
                Toast.makeText(
                    requireContext(),
                    resultData.getString(Constant.RESULT_DATA_KEY),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun fetchaddressfromlocation(location: Location) {
        val intent = Intent(requireContext(), FetchAddressServices::class.java)
        intent.putExtra(Constant.RECEVIER, resultReceiver)
        intent.putExtra(Constant.LOCATION_DATA_EXTRA, location)
        requireContext().startService(intent)
    }

    fun loadmap(loc: String, lat: String, Lon: String) {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.myMap_frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear() //clear old markers

            val googlePlex = CameraPosition.builder()
                .target(LatLng(lat.toDouble(), Lon.toDouble()))
                .zoom(12f)
              //  .bearing(0f)
                .tilt(45f)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null)
            val height = 100
            val width = 100
            val bitmapdraw = resources.getDrawable(R.drawable.placeholder) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(lat.toDouble(), Lon.toDouble()))
                    .title(loc)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            )


        }

    }

}




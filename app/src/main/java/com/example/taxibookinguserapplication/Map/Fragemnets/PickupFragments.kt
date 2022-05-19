package com.example.taxibookinguserapplication.Map.Fragemnets

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.taxibookinguserapplication.Map.Adapter.SearchActivityNew
import com.example.taxibookinguserapplication.Map.Pick_up
import com.example.taxibookinguserapplication.Map.Vechicle_list
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.util.Constant
import com.example.taxibookinguserapplication.util.FetchAddressServices
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.rehablab.util.ConstantUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pick_up.*
import kotlinx.android.synthetic.main.pickup_fragments.*
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class PickupFragments : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var locat: String = ""
    var lati_curr: String = ""
    var longi_current: String = ""
    var pick_up_location: String = ""
    var drop_location: String = ""
    var n_of_passenger: String = ""
    var lati_drop: String = ""
    var langit_drop: String = ""
    var sourlat = 0.0
    var sourlng:Double = 0.0
    var deslat = 0.0
    var deslng:Double = 0.0
    var destLoc=""

    var locType:String=""
    var placesClient: PlacesClient? = null
    var Image_Url:String=""
    lateinit var img_profile:ImageView

    var autoCompleteTextView_drop: AutoCompleteTextView? = null
    var adapter: AutoCompleteAdapter? = null
    var adapter_1:AutoCompleteAdapter_pickup?=null

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var pick_up_user: AutoCompleteTextView
    var resultReceiver: ResultReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootview = inflater.inflate(R.layout.pickup_fragments, container, false)





        resultReceiver = AddressResultReceiver(Handler())
        pick_up_user = rootview.findViewById(R.id.pickup_location_user)
        img_profile=rootview.findViewById(R.id.img_view_frg)
       /* pick_up_user.setOnClickListener {
            locType = "pickloc"

            var intent = Intent(requireContext(), SearchActivityNew::class.java)
            startActivityForResult(intent, 100)
        }*/



        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )

            Toast.makeText(requireContext(),"Permission", Toast.LENGTH_LONG).show()
        } else {
            currentLocation
        }
        Image_Url=
            SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Image_Url,"").toString()
        if (Image_Url.equals(""))
        {
            val  picasso= Picasso.get()
            picasso.load(R.drawable.driverimg).into(img_profile)
        }
        else
        {
            val  picasso= Picasso.get()
            picasso.load(Image_Url).into(img_profile)
        }
        val apiKey = getString(R.string.api_key)



        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        placesClient = Places.createClient(requireContext())
        autoCompleteTextView_drop = rootview.findViewById<AutoCompleteTextView>(R.id.drop_location_user_automatic)
        autoCompleteTextView_drop?.setOnClickListener {
            locType = "droploc"

            var intent = Intent(requireContext(), SearchActivityNew::class.java)
            startActivityForResult(intent, 100)

        }


        initAutoCompleteTextView_drop()
       // initAutoCompleteTextView_pickup()



        var ivMenu1: ImageView = rootview.findViewById(R.id.img_view_frg)
        ivMenu1.setOnClickListener {
            (activity as Pick_up)?.click1()
        }


        return rootview
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 100) {
                val lat: String = data?.getStringExtra("lat").toString()
                val lng: String = data?.getStringExtra("lng").toString()
                val location: String = data?.getStringExtra("location").toString()
                if(locType.equals("pickloc")){
                    sourlat=lat.toDouble()
                    sourlng=lng.toDouble()
                    pick_up_user?.setText(location)
                    pick_up_user?.setText(location)
                    /*SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.Drop_Off_Location, location)
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.Drop_Off_Latitude, sourlat.toString())
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.Drop_Off_Longitude, sourlng.toString())*/

                   // sourcelatLng= LatLng(sourlat,sourlng)
                }else{
                    deslat=lat.toDouble()
                    deslng=lng.toDouble()
                    destLoc=location
                  //  destlatLng= LatLng(deslat,deslng)
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.Drop_Off_Location, location)
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.Drop_Off_Latitude, deslat.toString())
                    SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                        ConstantUtils.Drop_Off_Longitude, deslng.toString())

                    autoCompleteTextView_drop?.setText(location)
                    autoCompleteTextView_drop?.setText(location)
                    val intent=Intent(requireContext(),Vechicle_list::class.java)
                    startActivity(intent)
                }



            }

        }catch (e:Exception){

        }

    }

    private fun bitmapDescriptorFromVector(context: Context?, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {


    }


    private fun initAutoCompleteTextView_drop() {
        autoCompleteTextView_drop?.setThreshold(1)
        autoCompleteTextView_drop?.setOnItemClickListener(autocompleteClickListener_drop)
        adapter = AutoCompleteAdapter(requireContext(), placesClient)
        autoCompleteTextView_drop?.setAdapter(adapter)
    }
   /* private fun initAutoCompleteTextView_pickup() {
        pick_up_user?.setThreshold(1)
        pick_up_user?.setOnItemClickListener(autocompleteClickListener_pickup)
        adapter_1 = AutoCompleteAdapter_pickup(requireContext(), placesClient)
        pick_up_user?.setAdapter(adapter_1)
    }*/

    private val autocompleteClickListener_drop =
        AdapterView.OnItemClickListener { adapterView, view, i, l ->
            try {
                val item: AutocompletePrediction = adapter?.getItem(i)!!
                var placeID: String? = null
                if (item != null) {
                    placeID = item.placeId
                }

                val placeFields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
                )
                var request: FetchPlaceRequest? = null
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                        .build()
                }
                if (request != null) {
                    placesClient!!.fetchPlace(request).addOnSuccessListener { task ->
                        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(drop_location_user_automatic.getWindowToken(), 0)

                        drop_location = drop_location_user_automatic.text.toString()
                        SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils.Drop_Off_Location,drop_location)
                        getLocationFromAddress_drop(drop_location)

                    }.addOnFailureListener { e ->
                        e.printStackTrace()

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private val autocompleteClickListener_pickup =
        AdapterView.OnItemClickListener { adapterView, view, i, l ->
            try {
                val item: AutocompletePrediction = adapter_1?.getItem(i)!!
                var placeID: String? = null
                if (item != null) {
                    placeID = item.placeId
                }
                val placeFields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
                )
                var request: FetchPlaceRequest? = null
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                        .build()
                }
                if (request != null) {
                    placesClient!!.fetchPlace(request).addOnSuccessListener { task ->


                        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(pick_up_user.getWindowToken(), 0)

                        pick_up_location = pick_up_user.text.toString()


                        getLocationFromAddress_pickup(pick_up_location)

                    }.addOnFailureListener { e ->
                        e.printStackTrace()
                        Toast.makeText(requireContext(),e.printStackTrace().toString(), Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(),e.printStackTrace().toString(), Toast.LENGTH_LONG).show()
            }
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
                                val lati = locationResult.locations[latestlocIndex].latitude
                                val longi = locationResult.locations[latestlocIndex].longitude
                                lati_curr = lati.toString()
                                longi_current = longi.toString()
                                SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                                    ConstantUtils.Pick_UP_Latitude, lati_curr)
                                SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                                    ConstantUtils.Pick_UP_Longitude, longi_current)

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
                locat = address + "," + locaity + "," + state
                pick_up_user.setText(locat)
                SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                    ConstantUtils.Pick_up_Location, locat)

                loadMap(lati_curr, longi_current,locat)

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

    fun loadMap(lati_curr1: String, longi_current1: String,loate1:String) {
        try {
            if (lati_curr.isEmpty() || longi_current.isEmpty()) {

            } else {

                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?
                mapFragment!!.getMapAsync { mMap ->
                    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                    mMap.clear()


                    val googlePlex = CameraPosition.builder()
                        .target(LatLng(lati_curr1.toDouble(), longi_current1.toDouble()))
                        .zoom(12f)
                        .bearing(0f)
                        .build()

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)

                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(lati_curr1.toDouble(), longi_current1.toDouble()))
                            .title(loate1)
                            .icon(bitmapDescriptorFromVector(activity, R.drawable.location_iocon))
                    )
                }


            }

        } catch (e: Exception) {

        }


    }

    fun getLocationFromAddress_drop(strAddress: String?) {

        val coder = Geocoder(requireContext())
        val address: List<Address>?
        try {

            address = coder.getFromLocationName(strAddress, 5)


            if (address == null) {
                return
            }


            val location = address[0]
            val latLng = LatLng(location.latitude, location.longitude)
            var la_longArr = latLng.toString().split(",", "(", ")")
            lati_drop = la_longArr[1]
            langit_drop = la_longArr[2]

            Log.d("daad", lati_drop + langit_drop)
            if (strAddress != null) {
                loadMap(lati_drop, langit_drop,strAddress)
            }

            val intent=Intent(requireContext(),Vechicle_list::class.java)
            startActivity(intent)


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getLocationFromAddress_pickup(strAddress: String?) {

        val coder = Geocoder(requireContext())
        val address: List<Address>?
        try {
            address = coder.getFromLocationName(strAddress, 5)

            if (address == null) {
                return
            }

            val location = address[0]
            val latLng = LatLng(location.latitude, location.longitude)
            var la_longArr = latLng.toString().split(",", "(", ")")
            lati_curr = la_longArr[1]
            longi_current = la_longArr[2]
            Toast.makeText(requireContext(),lati_curr+longi_current, Toast.LENGTH_LONG).show()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Pick_up_Location, strAddress)
           SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Pick_UP_Latitude, lati_curr)
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Pick_UP_Longitude, longi_current)

            Log.d("daad", lati_curr + longi_current)
            if (strAddress != null) {
                loadMap(lati_curr, longi_current,strAddress)
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getKilometers(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val PI_RAD = Math.PI / 180.0
        val phi1 = lat1 * PI_RAD
        val phi2 = lat2 * PI_RAD
        val lam1 = long1 * PI_RAD
        val lam2 = long2 * PI_RAD
        return 6371.01 * Math.acos(
            Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(
                lam2 - lam1
            )
        )
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }



}
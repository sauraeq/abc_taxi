package com.example.taxibookinguserapplication.Map.Fragemnets

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.LocationMap.Location_fetchActivity
import com.example.taxibookinguserapplication.Main.TipInformation
import com.example.taxibookinguserapplication.Map.ConfirmPickUP
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.BookingResponse
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
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
import kotlinx.android.synthetic.main.confirm_pickupfragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConfirmPickUpFragemnt : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var locat:String=""
    lateinit var current_location: AutoCompleteTextView
    var placesClient: PlacesClient? = null
    var Current_lati:String=""
    var Current_longi:String=""
    var Current_location:String=""
    var drop_lati:String=""
    var drop_longi:String=""
    var pickup_cnf_location:String=""
    var total_time:String=""
    var total_distance:String="0"
    var adapter: AutoCompleteAdapter? = null
    lateinit var customprogress: Dialog
    lateinit var confirm_search_btn: TextView
    lateinit var pick_up_confirm_texview: TextView
    lateinit var rest_loc:TextView
    var Confirm_pickup_total_distance:String=""
    var toatal_time_taken:String="0"
    var Drop_address:String=""
    var user_id:String=""
    var driver_id:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootview= inflater.inflate(R.layout.confirm_pickupfragment, container, false)
        confirm_search_btn=rootview.findViewById(R.id.confirm_search_pickup)
        var  back_go_activityy=rootview.findViewById<LinearLayout>(R.id.back_go_activity)
        var  confirm_pick_up=rootview.findViewById<LinearLayout>(R.id.confirm_pick_Up_layout)
        current_location=rootview.findViewById(R.id.current_loc_textview)
        pick_up_confirm_texview =rootview.findViewById(R.id.pick_up_confirm_txt1)
        rest_loc=rootview.findViewById(R.id.reset_location)

        customprogress= Dialog(requireContext())
        customprogress.setContentView(R.layout.loaderrrr_layout)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
        rest_loc.setOnClickListener {
            SharedPreferenceUtils.getInstance(requireContext())!!.removeKey(ConstantUtils.Pick_UP_Longitude)
            SharedPreferenceUtils.getInstance(requireContext())!!.removeKey(ConstantUtils.Pick_UP_Latitude)
            SharedPreferenceUtils.getInstance(requireContext())!!.removeKey(ConstantUtils.Pick_up_Location)
            SharedPreferenceUtils.getInstance(requireContext())!!.removeKey(ConstantUtils.Drop_Off_Location)
            SharedPreferenceUtils.getInstance(requireContext())!!.removeKey(ConstantUtils.Drop_Off_Latitude)
            SharedPreferenceUtils.getInstance(requireContext())!!.removeKey(ConstantUtils.Drop_Off_Location)
            SharedPreferenceUtils.getInstance(requireContext())!!.removeKey(ConstantUtils.Driver_id)
            SharedPreferenceUtils.getInstance(requireContext())!!.removeKey(ConstantUtils.Booking_id)
            var intent=Intent(requireContext(),Location_fetchActivity::class.java)
            startActivity(intent)
        }


        locat= SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Pick_up_Location,"").toString()
        current_location.setText(locat)



        val apiKey = getString(R.string.api_key)



        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        placesClient = Places.createClient(requireContext())

        initAutoCompleteTextView()
        back_go_activityy.setOnClickListener {

            (activity as ConfirmPickUP)?.inte()

        }
        confirm_pick_up.setOnClickListener {

        }



        try {
            driver_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Driver_id,"").toString()

            user_id=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.USER_ID,"").toString()
            total_time=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Total_Time,"").toString()
            total_distance=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Total_distance,"").toString()
            Drop_address=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Drop_Off_Location,"").toString()
            Current_location=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Pick_up_Location,"").toString()
           Current_longi =SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Pick_UP_Longitude,"").toString()
            Current_lati=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Pick_UP_Latitude,"").toString()
            drop_longi=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Drop_Off_Longitude,"").toString()
            drop_lati=SharedPreferenceUtils.getInstance(requireContext())?.getStringValue(ConstantUtils.Drop_Off_Latitude,"").toString()

            loadmap(Current_location,Current_lati,Current_longi)

        }
        catch (e:Exception)
        {

        }



        pick_up_confirm_texview.setOnClickListener {


             if (Current_lati.isEmpty())
            {
                Toast.makeText(requireContext(),"confirm pickup latitude empty", Toast.LENGTH_LONG)
                    .show()
            }
            else if(Current_longi.isEmpty())
            {
                Toast.makeText(requireContext(),"confirm pickup longitude empty", Toast.LENGTH_LONG)
                    .show()
            }
            else
            {
                Confirm_Booking()


            }
        }


        return rootview
    }

    private fun bitmapDescriptorFromVector(context: Context?, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap =
            Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {


    }

    private fun initAutoCompleteTextView() {
        current_location?.setThreshold(1)
        current_location?.setOnItemClickListener(autocompleteClickListener_drop)
        adapter = AutoCompleteAdapter(requireContext(), placesClient)
        current_location?.setAdapter(adapter)
    }

    fun loadmap(var1:String,var_2:String,var_3:String)
    {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear()

            val googlePlex = CameraPosition.builder()
                .target(LatLng(var_2.toDouble(),var_3.toDouble()))
                .zoom(20f)
                .bearing(0f)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)
            val height = 90
            val width = 90
            val bitmapdraw = resources.getDrawable(R.drawable.placeholder) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(var_2.toDouble(),var_3.toDouble()))
                    .title(var1)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            )

        }
    }


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
                        inputMethodManager.hideSoftInputFromWindow(current_loc_textview.getWindowToken(), 0)

                        pickup_cnf_location = current_loc_textview.text.toString()
                        SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                            ConstantUtils.Pick_up_Location, pickup_cnf_location)
                        getLocationFromAddress(pickup_cnf_location)

                    }.addOnFailureListener { e ->
                        e.printStackTrace()

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun getLocationFromAddress(strAddress: String?) {

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
            Current_lati = la_longArr[1]
            Current_longi = la_longArr[2]
            var dis_confirm_pick_up=getKilometers(Current_lati.toDouble(),
                Current_longi.toDouble(),drop_lati.toDouble(),drop_longi.toDouble())
            total_distance=roundOffDecimal(dis_confirm_pick_up.toDouble()).toString()

            Totaltimetaken(total_distance.toDouble())

            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils.Total_distance,total_distance)
            //SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils.Distance,total_distance)



            Toast.makeText(requireContext(),Current_lati+Current_longi
                , Toast.LENGTH_LONG).show()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Pick_UP_Latitude, Current_lati)
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(
                ConstantUtils.Pick_UP_Longitude, Current_longi)

            Log.d("daad", Current_lati + Current_longi)
            if (strAddress != null) {
                loadMap(Current_lati, Current_longi,strAddress)
            }

            //  Confirm_Booking(pickup_cnf_location,confirm_pickup_latitude,confirm_pickup_longitude)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadMap(lati_curr1: String, longi_current1: String,loate1:String) {
        try {
            if (lati_curr1.isEmpty() || longi_current1.isEmpty()) {

            } else {

                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
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
                            .icon(bitmapDescriptorFromVector(activity, R.drawable.maparroww))
                    )
                }


            }

        } catch (e: Exception) {

        }


    }


   fun Confirm_Booking(){

        customprogress.show()
        val request = HashMap<String, String>()
        request.put("pickupAddress", Current_location)
        request.put("pickupLatitude", Current_lati)
        request.put("pickupLongitude", Current_longi)
        request.put("dropAddress", Drop_address)
        request.put("dropLatitude", drop_lati)
        request.put("dropLongitude", drop_longi)
        request.put("user_id", user_id)
        request.put("driver_id",driver_id)
        request.put("amount", "20")
        request.put("time",total_time )
        request.put("distance",total_distance)




        var driver_vec_details: Call<BookingResponse> = APIUtils.getServiceAPI()!!.booking(request)

        driver_vec_details.enqueue(object : Callback<BookingResponse> {
            override fun onResponse(
                call: Call<BookingResponse>,
                response: Response<BookingResponse>
            ) {
                try {

                    if (response.body()!!.success.equals("true")) {

                         val intent=Intent(requireContext(),TipInformation::class.java)
                startActivity(intent)
                        SharedPreferenceUtils.getInstance(requireContext())!!
                            .setStringValue(ConstantUtils.Booking_id, response.body()!!.data[0].booking_id.toString())
                        Toast.makeText(requireContext(), response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()

                        customprogress.hide()


                    } else {

                        Toast.makeText(requireContext(), response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                } catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                customprogress.hide()


            }

        })
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

    fun Totaltimetaken(distance_km: Double): String {


        val km = distance_km.toInt()
        val kms_per_min = 0.4
        val mins_taken = km / kms_per_min
        val totalMinutes = mins_taken.toInt()
        if (totalMinutes < 60) {

            total_time = totalMinutes.toString() + " " + "Mins"

            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils.Total_Time,total_time)
        } else {
            var minutes = Integer.toString(totalMinutes % 60)
            minutes = if (minutes.length == 1) "0$minutes" else minutes
            (totalMinutes / 60).toString() + " hour " + minutes + "mins"
            total_time = minutes.toString()
            SharedPreferenceUtils.getInstance(requireContext())!!.setStringValue(ConstantUtils.Total_Time,total_time)

        }
        return total_time

    }



}
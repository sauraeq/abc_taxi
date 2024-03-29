package com.example.taxibookinguserapplication.Main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.LocationMap.Location_fetchActivity
import com.example.taxibookinguserapplication.Payment.Payment_method
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.BookingStatusResponse
import com.example.taxibookinguserapplication.Responses.CancelTripResponse
import com.example.taxibookinguserapplication.Responses.MapData
import com.example.taxibookinguserapplication.Responses.RatingReviewResponse
import com.example.taxibookinguserapplication.util.NetworkUtils
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.rehablab.util.ConstantUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tip_information.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TipInformation : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var originLatitude: String = ""
    var originLongitude: String = ""
    var destinationLatitude: String = ""
    var destinationLongitude: String = ""
    var booking_id:String=""
    var approx_km:String=""
    var toatal_time_taken:String=""
    var user_id:String=""
    var driver_id:String=""
    lateinit var navigation_rv: RecyclerView
    lateinit var ivClose1: ImageView
    lateinit var customprogress: Dialog
    var amount:String=""
    var total_km:String=""
    var rating_val:String=""
    var reveiw_string_value:String=""
    var driver_mobile_number=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_information)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.dialog_progress)
        progess_linear.visibility=View.VISIBLE
        SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Activity_Status,"5")
        Cancel_booking_btn_aty.setOnClickListener {
            val intent=Intent(this,CancelRide::class.java)
            startActivity(intent)
        }

        try {
           // driver_mobile_number=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Driver_Mobile,"").toString()
            user_id=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.USER_ID,"").toString()
            driver_id=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Driver_id,"").toString()
            approx_km=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Total_distance,"").toString()
            toatal_time_taken=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Total_Time,"").toString()
            originLatitude=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Pick_UP_Latitude,"").toString()
            originLongitude=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Pick_UP_Longitude,"").toString()
            destinationLatitude=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Drop_Off_Latitude,"").toString()
            destinationLongitude=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Drop_Off_Longitude,"").toString()
            booking_id=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Booking_id,"").toString()
        } catch (e: Exception) {

        }
        if (NetworkUtils.checkInternetConnection(this))
        {
            Booking_status()
        }
        textview_cancel.setOnClickListener {
            CancelTripSubmit()
          /*  var intent=Intent(this,Location_fetchActivity::class.java)
            startActivity(intent)*/
        }
        payment_trip_information.setOnClickListener {
            var intent=Intent(this,Payment_method::class.java)
            startActivity(intent)
        }

        driver_call.setOnClickListener {

            if(driver_mobile_number.equals(""))
            {
                Toast.makeText(this,"Mobile Number empty ",Toast.LENGTH_LONG).show()

            }
            else{
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:" +driver_mobile_number) //change the number
                startActivity(callIntent)
            }


        }


        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)

        val value=getString(R.string.api_key)
        val apiKey = value.toString()


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_aty) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.getMapAsync {
            mMap = it
            val originLocation = LatLng(originLatitude.toDouble(), originLongitude.toDouble())
            mMap.addMarker(MarkerOptions().position(originLocation))
            val destinationLocation = LatLng(destinationLatitude.toDouble(), destinationLongitude.toDouble())
            mMap.addMarker(MarkerOptions().position(destinationLocation))
            val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
            GetDirection(urll).execute()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 10F))
        }

    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0!!
        val originLocation = LatLng(originLatitude.toDouble(), originLongitude.toDouble())
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 5F))
    }
    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(8f)
                lineoption.color(Color.BLACK)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }
    fun Booking_status()
    {
        /*customprogress.show()*/
        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)

        var driver_vec_details: Call<BookingStatusResponse> = APIUtils.getServiceAPI()!!.booking_status(request)

        driver_vec_details.enqueue(object : Callback<BookingStatusResponse> {
            override fun onResponse(call: Call<BookingStatusResponse>, response: Response<BookingStatusResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        if(response.body()!!.data[0].cancel.equals("1"))
                        {

                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Booking_id)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Driver_id)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Drop_Off_Location)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Drop_Off_Latitude)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Drop_Off_Longitude)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Total_distance)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Total_Time)

                            var intent=Intent(this@TipInformation,Location_fetchActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        else if (response.body()!!.data[0].status.equals("0")|| response.body()!!
                                    .data[0].status.equals("1"))

                            {


                                var handler: Handler? = null
                                handler = Handler()
                                handler.postDelayed(Runnable {
                                    Booking_status()

                                }, 5000)


                            }
                        else

                        {

                          /*  customprogress.hide()*/
                            progess_linear.visibility=View.GONE
                            trip_details_linear.visibility=View.VISIBLE
                            var otp=response.body()!!.data[0].otp
                            var driver_profile_pic=response.body()!!.data[0].profile_photo
                            var vechile_img=response.body()!!.data[0].vehicle_image
                            var vehicle_no=response.body()!!.data[0].vehicle_no
                            var name=response.body()!!.data[0].name
                            var vehicle_name=response.body()!!.data[0].vehicle_name
                            var rating=response.body()!!.data[0].rating


                            val picasso = Picasso.get()
                            if (vechile_img.equals(""))
                            {
                                picasso.load(R.drawable.driverimg).into(vch_img_drvFrg_aty)
                            }
                            else
                            {
                                picasso.load(vechile_img).into(vch_img_drvFrg_aty)
                            }
                            if (driver_profile_pic.equals(""))
                            {
                                picasso.load(R.drawable.car_i).into(driver_img_drvFrg_aty)
                            }
                            else{
                                picasso.load(driver_profile_pic).into(driver_img_drvFrg_aty)
                            }

                           /* picasso.load(vechile_img).into(vch_img_drvFrg_aty)*/
                            driver_nmae_drvFrg_aty.setText(name)
                            vch_name_drvFrg_aty.setText(vehicle_name)
                            vechile_number_drvFrg_aty.setText(vehicle_no)
                            otp_drvFrg_aty.setOTP(otp)
                            driver_rating_txt_aty.setText(rating)
                            tp_driverdetails.setText("CHF"+" "+response.body()!!.data[0].amount)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.setStringValue(ConstantUtils.Amount,response.body()!!.data[0].amount)
                            total_distancee_driverdetails.setText(response.body()!!.data[0].distance+" Km")

                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.setStringValue(ConstantUtils.Driver_Mobile,response.body()!!.data[0].mobile)
                            total_timee_driverdetails.setText(response.body()!!.data[0].time)
                            driver_mobile_number=response.body()!!.data[0].mobile
                          //  Toast.makeText(this@TipInformation,response.body()!!.data[0].mobile, Toast.LENGTH_LONG).show()
                           start_staus()

                        }

                    }
                    else {

                        Toast.makeText(this@TipInformation,response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@TipInformation,e.toString(), Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingStatusResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@TipInformation,t.message.toString(), Toast.LENGTH_LONG).show()
                customprogress.hide()

            }

        })
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    fun Ride_status()
    {

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)

        var driver_vec_details: Call<BookingStatusResponse> = APIUtils.getServiceAPI()!!.booking_status(request)

        driver_vec_details.enqueue(object : Callback<BookingStatusResponse> {
            override fun onResponse(call: Call<BookingStatusResponse>, response: Response<BookingStatusResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        if (response.body()!!.data[0].status.equals("3"))

                        {
                            showDialog()

                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Booking_id)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Driver_id)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Drop_Off_Location)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Drop_Off_Latitude)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Drop_Off_Longitude)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Total_distance)
                            SharedPreferenceUtils.getInstance(this@TipInformation)!!.removeKey(ConstantUtils.Total_Time)


                        }

                        else

                        {
                            var handler: Handler? = null
                            handler = Handler()
                            handler.postDelayed(Runnable {
                                Ride_status()

                            }, 5000)

                        }

                    }
                    else {

                        Toast.makeText(this@TipInformation,response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@TipInformation,e.toString(), Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingStatusResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@TipInformation,t.message.toString(), Toast.LENGTH_LONG).show()
                customprogress.hide()

            }

        })
    }

    fun start_staus()
    {

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)

        var driver_vec_details: Call<BookingStatusResponse> = APIUtils.getServiceAPI()!!.booking_status(request)

        driver_vec_details.enqueue(object : Callback<BookingStatusResponse> {
            override fun onResponse(call: Call<BookingStatusResponse>, response: Response<BookingStatusResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        if (response.body()!!.data[0].otp_status.equals("1"))

                        {
                            Ride_status()
                            progess_linear.visibility=View.GONE
                            trip_details_linear.visibility=View.VISIBLE
                            otp_linearlayout.visibility=View.GONE
                            linear_arriving.visibility=View.GONE
                            otp_text_linearlayout.visibility=View.GONE
                            var otp=response.body()!!.data[0].otp
                            var driver_profile_pic=response.body()!!.data[0].profile_photo
                            var vechile_img=response.body()!!.data[0].vehicle_image
                            var vehicle_no=response.body()!!.data[0].vehicle_no
                            var name=response.body()!!.data[0].name
                            var vehicle_name=response.body()!!.data[0].vehicle_name
                            var rating=response.body()!!.data[0].rating




                            val picasso = Picasso.get()
                            if (vechile_img.equals(""))
                            {
                                picasso.load(R.drawable.driverimg).into(vch_img_drvFrg_aty)
                            }
                            else
                            {
                                picasso.load(vechile_img).into(vch_img_drvFrg_aty)
                            }
                            if (driver_profile_pic.equals(""))
                            {
                                picasso.load(R.drawable.car_i).into(driver_img_drvFrg_aty)
                            }
                            else{
                                picasso.load(driver_profile_pic).into(driver_img_drvFrg_aty)
                            }
                            driver_nmae_drvFrg_aty.setText(name)
                            vch_name_drvFrg_aty.setText(vehicle_name)
                            vechile_number_drvFrg_aty.setText(vehicle_no)
                            otp_drvFrg_aty.setOTP(otp)
                            driver_rating_txt_aty.setText(rating)
                            tp_driverdetails.setText("CHF"+" "+response.body()!!.data[0].amount)
                            total_distancee_driverdetails.setText(response.body()!!.data[0].distance+" Km")

                           // SharedPreferenceUtils.getInstance(this@TipInformation)!!.setStringValue(ConstantUtils.Driver_Mobile,response.body()!!.data[0].mobile)
                            total_timee_driverdetails.setText(response.body()!!.data[0].time)


                        }

                        else

                        {
                            var handler: Handler? = null
                            handler = Handler()
                            handler.postDelayed(Runnable {
                                start_staus()

                            }, 5000)

                        }

                    }
                    else {

                        Toast.makeText(this@TipInformation,response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@TipInformation,e.toString(), Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingStatusResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@TipInformation, t.message.toString(), Toast.LENGTH_LONG).show()
                customprogress.hide()

            }

        })
    }

    fun showDialog() {
        val dialog = BottomSheetDialog(this)

        //  val dialog = Dialog(mContext,android.R.style.ThemeOverlay_Material_Light)

        dialog.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);



        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_write_review)

        /* var booking_id=SharedPreferenceUtils.getInstance(mContext)?.getStringValue(ConstantUtils.Booking_id,"").toString()
         var driver_id=SharedPreferenceUtils.getInstance(mContext)?.getStringValue(ConstantUtils.Driver_Id,"").toString()
         var user_id=SharedPreferenceUtils.getInstance(mContext)?.getStringValue(ConstantUtils.USER_ID,"").toString()*/


        var reveiw_edittext=dialog.findViewById<EditText>(R.id.edit_review)
        var rating_bar=dialog.findViewById<RatingBar>(R.id.rBar)

        var submit_review_btn=dialog.findViewById<TextView>(R.id.submit_review_btn)

        submit_review_btn?.setOnClickListener {
             reveiw_string_value=reveiw_edittext?.text.toString()
             rating_val= rating_bar?.rating.toString()

            if (reveiw_string_value.isEmpty())
            {
                Toast.makeText(this,"Please Write Review",Toast.LENGTH_LONG).show()
            }
            else if (rating_val.equals("0.0"))
            {
                Toast.makeText(this,"Please rate your Review",Toast.LENGTH_LONG).show()
            }

            else
            {
                writereveiw()
                dialog.hide()

            }

        }




        dialog.show()

    }
    fun writereveiw()
    {

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)
        request.put("user_id",user_id)
        request.put("driver_id",driver_id)
        request.put("rating",rating_val)
        request.put("review",reveiw_string_value)





        var login_in: Call<RatingReviewResponse> = APIUtils.getServiceAPI()!!.rating(request)

        login_in.enqueue(object : Callback<RatingReviewResponse> {
            override fun onResponse(call: Call<RatingReviewResponse>, response: Response<RatingReviewResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        Toast.makeText(this@TipInformation,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()
                        var intent=Intent(this@TipInformation,Location_fetchActivity::class.java)
                        startActivity(intent)



                    } else {

                        Toast.makeText(this@TipInformation,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()

                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@TipInformation,"Weak Internet Connection", Toast.LENGTH_LONG).show()


                }

            }

            override fun onFailure(call: Call<RatingReviewResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@TipInformation,"Weak Internet Connection", Toast.LENGTH_LONG).show()

            }

        })
    }

    fun CancelTripSubmit()
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)
        request.put("description","Waiting for long Time")



        var cancel_trip: Call<CancelTripResponse> = APIUtils.getServiceAPI()!!.CancelResultSubmission(request)

        cancel_trip.enqueue(object : Callback<CancelTripResponse> {
            override fun onResponse(call: Call<CancelTripResponse>, response: Response<CancelTripResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        Toast.makeText(this@TipInformation,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()
                        var Inte= Intent(this@TipInformation,Location_fetchActivity::class.java)
                        startActivity(Inte)
                        finish()


                        customprogress.hide()




                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    customprogress.hide()


                }

            }

            override fun onFailure(call: Call<CancelTripResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                customprogress.hide()


            }

        })
    }


}
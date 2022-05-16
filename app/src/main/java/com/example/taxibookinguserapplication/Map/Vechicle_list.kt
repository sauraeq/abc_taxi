package com.example.taxibookinguserapplication.Map

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.Main.Adapter.TCAdapter
import com.example.taxibookinguserapplication.Map.Adapter.VehicleListAdapter
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.*
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
import com.google.gson.Gson
import com.rehablab.util.ConstantUtils
import kotlinx.android.synthetic.main.activity_vechicle_list.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat

class Vechicle_list : AppCompatActivity(),OnMapReadyCallback,VehicleListAdapter.PractiseInterface  {
    lateinit var recycler_Vehicle_list: RecyclerView
    private var mlist: List<VehicleResponseData> = ArrayList()
    private lateinit var mMap: GoogleMap
    var originLatitude: String = ""
    var originLongitude: String = ""
    var img_url: String = ""
    var distance: String = "0"
    var distance_Km:String="0"
    var destinationLatitude: String = ""
    var destinationLongitude: String = ""
    lateinit var customprogress: Dialog
    var total_time:String=""
    var driver_idd:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vechicle_list)
        supportActionBar?.hide()
        recycler_Vehicle_list=findViewById(R.id.recyler_vehicle_List)

        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.dialog_progress)

        originLatitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Pick_UP_Latitude, "").toString()
        originLongitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Pick_UP_Longitude, "").toString()
      /*  distance = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Distance, "").toString()*/
        destinationLatitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Drop_Off_Latitude, "").toString()
        destinationLongitude = SharedPreferenceUtils.getInstance(this)
            ?.getStringValue(ConstantUtils.Drop_Off_Longitude, "").toString()

        distance_Km=getKilometers(originLatitude.toDouble(),originLongitude.toDouble(),destinationLatitude.toDouble(),destinationLongitude.toDouble()).toString()
           distance=roundOffDecimal(distance_Km.toDouble()).toString()
        Totaltimetaken(distance.toDouble())
        SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Total_distance,distance)

        if(NetworkUtils.checkInternetConnection(this))
        {
            Vehiclelist()
        }

        back_linera_layout_act.setOnClickListener {
            onBackPressed()
        }


        /*confirm_txt.setOnClickListener {

        }*/
       /* pick_up_confirm_act.setOnClickListener {

            *//*val intent= Intent(this,ConfirmPick_up::class.java)
            startActivity(intent)*//*
        }*/

        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)

        val value=getString(R.string.api_key)
        val apiKey = value.toString()


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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

    override fun onclick(driver_id: String) {
        driver_idd=driver_id
        /*  Toast.makeText(this,reason,Toast.LENGTH_LONG).show()*/
        if (driver_idd.isEmpty()) {

        } else {
            SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Driver_id,driver_idd)
            confirm_txt.setOnClickListener {

                if (NetworkUtils.checkInternetConnection(this)) {
                   Vehiclelist()
                    val intent=Intent(this,ConfirmPickUP::class.java)
                    startActivity(intent)
                }
            }
        }



    }

    fun Vehiclelist()
    {
        customprogress.show()
        var hashMap = HashMap<String, String>()
        hashMap.put("latitude",originLatitude)
        hashMap.put("longitude", originLongitude)
        hashMap.put("distance",distance)
        var vehile_list: Call<VehicleListResponse> = APIUtils.getServiceAPI()!!.veh_list(hashMap)

        vehile_list.enqueue(object : Callback<VehicleListResponse> {
            override fun onResponse(call: Call<VehicleListResponse>, response: Response<VehicleListResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_Vehicle_list.layoutManager= LinearLayoutManager(this@Vechicle_list)
                        recycler_Vehicle_list.adapter= VehicleListAdapter(this@Vechicle_list,this@Vechicle_list,mlist)
                        customprogress.hide()

                    } else {
                        Toast.makeText(this@Vechicle_list,response.body()!!.msg, Toast.LENGTH_LONG).show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    customprogress.hide()
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@Vechicle_list,e.toString(), Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<VehicleListResponse>, t: Throwable) {
                customprogress.hide()
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@Vechicle_list,t.toString(), Toast.LENGTH_LONG).show()

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
            SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Total_Time,total_time)


        } else {
            var minutes = Integer.toString(totalMinutes % 60)
            minutes = if (minutes.length == 1) "0$minutes" else minutes
            (totalMinutes / 60).toString() + " hour " + minutes + "mins"
            total_time = minutes.toString()
            SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Total_Time,total_time)

        }
        return total_time

    }


}
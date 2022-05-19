package com.example.taxibookinguserapplication.Main

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.LocationMap.Location_fetchActivity
import com.example.taxibookinguserapplication.Main.Adapter.CancelTripAdapter
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.CancelRideReasonResponseData
import com.example.taxibookinguserapplication.Responses.CancelRideResonResponse
import com.example.taxibookinguserapplication.Responses.CancelTripResponse
import com.example.taxibookinguserapplication.util.NetworkUtils
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.rehablab.util.ConstantUtils
import kotlinx.android.synthetic.main.activity_cancel_ride.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CancelRide : AppCompatActivity(), CancelTripAdapter.PractiseInterface{

    lateinit var customprogress: Dialog
    private var mlist: List<CancelRideReasonResponseData> = ArrayList()
    lateinit var Cancel_trip_submit: TextView
    var reason:String=""
    var bookig_id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_ride)

        Cancel_trip_submit=findViewById(R.id.Cancel_trip_submit_btn)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loaderrrr_layout)
        CancelTripReson()
        bookig_id= SharedPreferenceUtils.getInstance(this)?.getStringValue(
            ConstantUtils
                .Booking_id,"")
            .toString()


        cancel_LeftArrow.setOnClickListener {
            onBackPressed()
        }





    }

    override fun onclick(name: String) {
        reason=name

        if (reason.isEmpty()) {

            Toast.makeText(this,"Please Select Cancellation Reason",Toast.LENGTH_LONG).show()

        } else {
            Cancel_trip_submit.setOnClickListener {

                if (NetworkUtils.checkInternetConnection(this)) {
                    CancelTripSubmit(reason)
                }
            }
        }



    }

    fun CancelTripReson()
    {
        customprogress.show()
        val request = HashMap<String, String>()



        var cancel_trip: Call<CancelRideResonResponse> = APIUtils.getServiceAPI()!!.CancelReason()

        cancel_trip.enqueue(object : Callback<CancelRideResonResponse> {
            override fun onResponse(call: Call<CancelRideResonResponse>, response: Response<CancelRideResonResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        rcyView_Cancel_trip.layoutManager= LinearLayoutManager(this@CancelRide)
                        rcyView_Cancel_trip.adapter= CancelTripAdapter(this@CancelRide,this@CancelRide,mlist)
                        // rcyView_Cancel_trip.findViewHolderForItemId()



                        customprogress.hide()




                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    customprogress.hide()


                }

            }

            override fun onFailure(call: Call<CancelRideResonResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                customprogress.hide()


            }

        })
    }

    fun CancelTripSubmit(var1:String)
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("booking_id",bookig_id)
        request.put("description",var1)



        var cancel_trip: Call<CancelTripResponse> = APIUtils.getServiceAPI()!!.CancelResultSubmission(request)

        cancel_trip.enqueue(object : Callback<CancelTripResponse> {
            override fun onResponse(call: Call<CancelTripResponse>, response: Response<CancelTripResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        Toast.makeText(this@CancelRide,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()
                        var Inte= Intent(this@CancelRide,Location_fetchActivity::class.java)
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
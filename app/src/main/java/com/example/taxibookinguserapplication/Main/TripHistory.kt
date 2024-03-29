package com.example.taxibookinguserapplication.Main

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.Main.Adapter.TCAdapter
import com.example.taxibookinguserapplication.Main.Adapter.TripHistory_adapter
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.*
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.rehablab.util.ConstantUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_trip_history.*
import kotlinx.android.synthetic.main.activity_view_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripHistory : AppCompatActivity() {
    lateinit var recycler_trip_history: RecyclerView
    private var mlist: List<TripHistoryData> = ArrayList()
     lateinit  var customprogress:Dialog
     var user_id:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loaderrrr_layout)
        setContentView(R.layout.activity_trip_history)
        recycler_trip_history=findViewById(R.id.recyclerview_trip_history)

        user_id=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.USER_ID,"").toString()
        back_act_triphistory.setOnClickListener {
            onBackPressed()
        }
        Triphistory()
    }


    fun Triphistory()
    {    customprogress.show()
        var hashMap = HashMap<String, String>()
        hashMap.put("user_id",user_id)


        var trips: Call<TripHistory_Response> = APIUtils.getServiceAPI()!!.trip_his(hashMap)

        trips.enqueue(object : Callback<TripHistory_Response> {
            override fun onResponse(call: Call<TripHistory_Response>, response: Response<TripHistory_Response>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_trip_history.layoutManager= LinearLayoutManager(this@TripHistory)
                        recycler_trip_history.adapter= TripHistory_adapter(this@TripHistory,mlist)
                        customprogress.hide()

                    } else {
                        Toast.makeText(this@TripHistory,response.body()!!.msg,Toast.LENGTH_LONG).show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    customprogress.hide()
                }

            }

            override fun onFailure(call: Call<TripHistory_Response>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                customprogress.hide()
            }

        })
    }
}
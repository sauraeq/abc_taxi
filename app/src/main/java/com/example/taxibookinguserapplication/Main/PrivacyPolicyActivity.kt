package com.example.taxibookinguserapplication.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.Main.Adapter.PrivacyAdapter
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.PrivacyResData
import com.example.taxibookinguserapplication.Responses.Privacy_Response
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class PrivacyPolicyActivity : AppCompatActivity() {

    lateinit var recycler_privacy: RecyclerView
    private var mlist: List<PrivacyResData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        recycler_privacy=findViewById(R.id.recycler_privacy_Terms)

        PrivacyPolicy()
        rel_canlemark.setOnClickListener {
            onBackPressed()
        }
    }

    fun PrivacyPolicy()
    {

        var privacy: Call<Privacy_Response> = APIUtils.getServiceAPI()!!.privacy()

        privacy.enqueue(object : Callback<Privacy_Response> {
            override fun onResponse(call: Call<Privacy_Response>, response: Response<Privacy_Response>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_privacy.layoutManager= LinearLayoutManager(this@PrivacyPolicyActivity)
                        recycler_privacy.adapter= PrivacyAdapter(this@PrivacyPolicyActivity,mlist)

                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                }

            }

            override fun onFailure(call: Call<Privacy_Response>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

            }

        })
    }
}
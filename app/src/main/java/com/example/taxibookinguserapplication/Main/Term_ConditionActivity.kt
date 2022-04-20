package com.example.taxibookinguserapplication.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.Main.Adapter.PrivacyAdapter
import com.example.taxibookinguserapplication.Main.Adapter.TCAdapter
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.PrivacyResData
import com.example.taxibookinguserapplication.Responses.Privacy_Response
import com.example.taxibookinguserapplication.Responses.TCResData
import com.example.taxibookinguserapplication.Responses.TCResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class Term_ConditionActivity : AppCompatActivity() {

    lateinit var recycler_privacy: RecyclerView
    private var mlist: List<TCResData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_condition)
        recycler_privacy=findViewById(R.id.recycler_termss)
        TermsConditionss()
    }

    fun TermsConditionss()
    {
        var terms: Call<TCResponse> = APIUtils.getServiceAPI()!!.TC()

        terms.enqueue(object : Callback<TCResponse> {
            override fun onResponse(call: Call<TCResponse>, response: Response<TCResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_privacy.layoutManager= LinearLayoutManager(this@Term_ConditionActivity)
                        recycler_privacy.adapter= TCAdapter(this@Term_ConditionActivity,mlist)


                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                }

            }

            override fun onFailure(call: Call<TCResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

            }

        })
    }
}
package com.example.taxibookinguserapplication.Main

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.example.taxibookinguserapplication.util.BaseActivity
import com.example.taxibookinguserapplication.util.cont
import kotlinx.android.synthetic.main.activity_term_condition.*
import kotlinx.android.synthetic.main.activity_view_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class Term_ConditionActivity : AppCompatActivity() {

    lateinit var recycler_privacy: RecyclerView
    private var mlist: List<TCResData> = ArrayList()
    lateinit var customprogress:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loaderrrr_layout)
        setContentView(R.layout.activity_term_condition)
        recycler_privacy=findViewById(R.id.recycler_termss)
        TermsConditionss()
        back_act_terms.setOnClickListener {
            onBackPressed()
        }

    }

    fun TermsConditionss()
    {
    customprogress.show()
        var terms: Call<TCResponse> = APIUtils.getServiceAPI()!!.TC()

        terms.enqueue(object : Callback<TCResponse> {
            override fun onResponse(call: Call<TCResponse>, response: Response<TCResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_privacy.layoutManager= LinearLayoutManager(this@Term_ConditionActivity)
                        recycler_privacy.adapter= TCAdapter(this@Term_ConditionActivity,mlist)

                        customprogress.hide()
                    } else {
                    Toast.makeText(this@Term_ConditionActivity,response.body()!!.msg,Toast.LENGTH_LONG).show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    customprogress.hide()
                }

            }

            override fun onFailure(call: Call<TCResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                customprogress.hide()
            }

        })
    }
}
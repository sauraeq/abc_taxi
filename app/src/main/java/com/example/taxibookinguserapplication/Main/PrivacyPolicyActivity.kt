package com.example.taxibookinguserapplication.Main

import android.app.Dialog
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
import com.example.taxibookinguserapplication.util.BaseActivity
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_privacy_policy.rel_canlemark
import kotlinx.android.synthetic.main.activity_view_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class PrivacyPolicyActivity : AppCompatActivity() {

    lateinit var recycler_privacy: RecyclerView
    private var mlist: List<PrivacyResData> = ArrayList()
    lateinit var customprogress:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loaderrrr_layout)
        setContentView(R.layout.activity_privacy_policy)
        recycler_privacy=findViewById(R.id.recycler_privacy_Terms)
        back_act_privacy.setOnClickListener {
            onBackPressed()
        }

        PrivacyPolicy()
        rel_canlemark.setOnClickListener {
            onBackPressed()
        }
    }

    fun PrivacyPolicy()
    {
customprogress.show()

        var privacy: Call<Privacy_Response> = APIUtils.getServiceAPI()!!.privacy()

        privacy.enqueue(object : Callback<Privacy_Response> {
            override fun onResponse(call: Call<Privacy_Response>, response: Response<Privacy_Response>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                        mlist= response.body()!!.data
                        recycler_privacy.layoutManager= LinearLayoutManager(this@PrivacyPolicyActivity)
                        recycler_privacy.adapter= PrivacyAdapter(this@PrivacyPolicyActivity,mlist)
                        customprogress.hide()
                    } else {
                        customprogress.hide()

                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    customprogress.hide()
                }

            }

            override fun onFailure(call: Call<Privacy_Response>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                customprogress.hide()

            }

        })
    }
}
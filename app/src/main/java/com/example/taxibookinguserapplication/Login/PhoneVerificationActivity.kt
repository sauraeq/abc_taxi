package com.example.taxibookinguserapplication.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.LocationMap.CurrentLocationActivity
import com.example.taxibookinguserapplication.LocationMap.Location_fetchActivity
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.LoginotpverifyResponse
import com.example.taxibookinguserapplication.util.BaseActivity
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.example.taxibookinguserapplication.util.cont
import com.rehablab.localsaved.shareprefrences
import com.rehablab.util.ConstantUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_phoneverification.*
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap


class PhoneVerificationActivity : BaseActivity(), cont {
    lateinit var shrp: shareprefrences
    var mobile: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phoneverification)

        shrp = shareprefrences(this)

        try {
            mobile = intent.getStringExtra("mobile").toString()
            /* type = intent.getStringExtra("type").toString()
             password = intent.getStringExtra("password").toString()*/
        } catch (e: Exception) {

        }

        tv_verify.setOnClickListener {
            /* val intent= Intent(this,ResetPasswordActivity::class.java)
             startActivity(intent)*/
            if (otp_view.otp!!.length == 4) {

                VerifyOtp()

            } else {
                showToastMessage(this, "Please Enter Otp")
            }
        }

    }

    private fun VerifyOtp() {
        showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        stringStringHashMap.put("mobile", mobile)
        stringStringHashMap.put("otp", otp_view.otp.toString())

        var otpverifyCall = APIUtils.getServiceAPI()?.otpverify(stringStringHashMap)
        otpverifyCall?.enqueue(object : retrofit2.Callback<LoginotpverifyResponse> {
            override fun onResponse(
                call: Call<LoginotpverifyResponse>,
                response: Response<LoginotpverifyResponse>
            ) {
                try {
                    hideProgressDialog()

                        if (response.body()!!.success == "true") {
                            SharedPreferenceUtils.getInstance(this@PhoneVerificationActivity)?.setStringValue(ConstantUtils.OTP,otp_view.otp.toString())
                            var intent=Intent(this@PhoneVerificationActivity,Location_fetchActivity::class.java)
                            startActivity(intent)
                        }
                    else
                        {
                            showToastMessage(this@PhoneVerificationActivity,response.body()!!.msg.toString())                        }

                    }

                 catch (e: Exception) {
                    Log.e("dfjhjdvf", e.toString())
                }

            }

            override fun onFailure(call: Call<LoginotpverifyResponse>, t: Throwable) {
                Log.e("dvhgd", t.message.toString())
                hideProgressDialog()
                showToastMessage(this@PhoneVerificationActivity, t.message.toString())
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
}
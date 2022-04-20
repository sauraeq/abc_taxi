package com.example.taxibookinguserapplication.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.LocationMap.Location_fetchActivity
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.SignUpResponse

import com.example.taxibookinguserapplication.Responses.SigninResponse
import com.example.taxibookinguserapplication.util.BaseActivity
import com.example.taxibookinguserapplication.util.cont
import com.rehablab.localsaved.shareprefrences
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity(), cont {

    lateinit var shrp: shareprefrences
    var language: String = ""
    var device_tokenid :String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        shrp = shareprefrences(this)

        try {
            language = intent.getStringExtra("language").toString()
            device_tokenid = intent.getStringExtra("device_tokenid").toString()
        } catch (e: Exception) { }

        tv_next.setOnClickListener {
       SigninCall()
            /* val intent = Intent(this, Location_fetchActivity::class.java)
             startActivity(intent)*/
        }

       /* tv_sign_up.setOnClickListener {
            val intent = Intent(this, Location_fetchActivity::class.java)
            startActivity(intent)
        }*/
        tvSiginTab2.setBackground(ContextCompat.getDrawable(this, R.drawable.greenovalshape));
        tvSignUpTab1.setOnClickListener {
            lnr_sinup.visibility = View.VISIBLE
            lnr_signin.visibility = View.GONE
            tvSignUpTab1.setBackground(ContextCompat.getDrawable(this, R.drawable.greenovalshape));
            tvSignUpTab1.setTextColor(getResources().getColor(R.color.white));
            tvSiginTab2.setTextColor(getResources().getColor(R.color.black));

            tvSiginTab2.setBackgroundResource(0);
        }
        tvSiginTab2.setOnClickListener {
            lnr_sinup.visibility = View.GONE
            lnr_signin.visibility = View.VISIBLE
            tvSiginTab2.setBackground(ContextCompat.getDrawable(this, R.drawable.greenovalshape));
            tvSiginTab2.setTextColor(getResources().getColor(R.color.white));

            tvSignUpTab1.setTextColor(getResources().getColor(R.color.black));
            tvSignUpTab1.setBackgroundResource(0);
        }
        tv_forgtpass.setOnClickListener {
            val intent = Intent(this, GetOtpActivity::class.java)
            startActivity(intent)
        }
        tv_sign_up.setOnClickListener {
            if (email_edit_text.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
            } else if (editText_carrierNumber.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show()
            } else {
                //SignupCallApi()
                signup()
            }
        }
    }

    private fun SigninCall() {
        showProgressDialog()
        var hashMap = HashMap<String, String>()
      //  var device_tokenid: String = shrp.getStringPreference(Token).toString()
        hashMap.put("mobile", editText1_carrierNumber.text.toString())
        hashMap.put("device_tokanid", device_tokenid)

        val SinginCall = APIUtils.getServiceAPI()?.signin(hashMap)
        SinginCall?.enqueue(object : retrofit2.Callback<SigninResponse> {
            override fun onResponse(
                call: Call<SigninResponse>,
                response: Response<SigninResponse>
            ) {
                try {
                    Log.e("signinresponse", response.body().toString())
                    hideProgressDialog()
                    if (response.code() == 200) {
                        if (response.body()?.success.equals("true")) {
                            if (response.body()!!.data.size > 0) {

                                var data = response.body()!!.data[0]
                                Toast.makeText(this@LoginActivity,"otp__"+data.otp,Toast.LENGTH_LONG).show()
                                shrp.setStringPreference(OTP, data.otp)
                                shrp.setStringPreference(DEVICE_TOKENID, data.device_tokanid)
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        PhoneVerificationActivity::class.java
                                    ).putExtra("mobile",editText1_carrierNumber.text.toString())
                                )

                            } else {
                                showToastMessage(
                                    this@LoginActivity,
                                    response.body()?.msg.toString()
                                )
                            }
                        }
                    }

                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                hideProgressDialog()
                Log.e("signinrfailuour", t.message.toString())
                showToastMessage(this@LoginActivity, t.message.toString())
            }
        })

    }

    private fun SignupCallApi() {

        showProgressDialog()
        var hashMap = HashMap<String, String>()
     //   var language: String = shrp.getStringPreference(LANGUAGE).toString()

        hashMap.put("mobile", editText_carrierNumber.text.toString())
        hashMap.put("email", email_edit_text.text.toString())
        hashMap.put("language", "1")

        val SingupCall = APIUtils.getServiceAPI()?.signup(hashMap)
        SingupCall?.enqueue(object : retrofit2.Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                try {
                    Log.e("signupresponse", response.body().toString())
                    hideProgressDialog()
                    if (response.code() == 200) {
                        if (response.body()?.success.equals("true")) {


                        }
                        else{
                            Toast.makeText(this@LoginActivity,response.body()!!.msg,Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        Toast.makeText(this@LoginActivity,response.body()!!.msg,Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {
                    Log.e("signuprfailuour", e.message.toString())
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                hideProgressDialog()
                Log.e("signuprfailuour", t.message.toString())
                showToastMessage(this@LoginActivity, t.message.toString())
            }
        })
    }
    fun signup()
    {
        showProgressDialog()
        val request = HashMap<String, String>()

        request.put("mobile", editText_carrierNumber.text.toString())
        request.put("email", email_edit_text.text.toString())
        request.put("language", "1")



        var signup: Call<SignUpResponse> = APIUtils.getServiceAPI()!!.signup(request)

        signup.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                try {

                    if (response.body()!!.success.equals("true")) {


                        Toast.makeText(this@LoginActivity,response.body()!!.msg.toString(),Toast.LENGTH_LONG).show()

                        var intent = Intent(this@LoginActivity, LoginActivity::class.java)

                        startActivity(intent)

                          hideProgressDialog()

                    } else {
                        hideProgressDialog()
                        Toast.makeText(this@LoginActivity,response.body()!!.msg,Toast.LENGTH_LONG).show()
                    }

                }  catch (e: Exception) {
                    hideProgressDialog()
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@LoginActivity,e.message,Toast.LENGTH_LONG).show()
                   // custom_progress.hide()

                }

            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                hideProgressDialog()

                Toast.makeText(this@LoginActivity,"Email  is already exit",Toast.LENGTH_LONG).show()
               // custom_progress.hide()

            }

        })
    }
}
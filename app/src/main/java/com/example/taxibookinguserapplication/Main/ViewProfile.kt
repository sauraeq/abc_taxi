package com.example.taxibookinguserapplication.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.Login.LoginActivity
import com.example.taxibookinguserapplication.Login.PhoneVerificationActivity
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.SignUpResponse
import com.example.taxibookinguserapplication.Responses.SigninResponse
import com.example.taxibookinguserapplication.util.BaseActivity
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.rehablab.util.ConstantUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_view_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewProfile : BaseActivity() {

    var phone_number:String=""
    var token_id:String=""
    var Image_Url:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        Image_Url=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Image_Url,"").toString()
        token_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Token_ID,"").toString()
        phone_number=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Phone_Number,"").toString()


        var picasso=Picasso.get()
        user_drtails()
        try {
            if (Image_Url!=null)
            {
                picasso.load(Image_Url).into(User_profile_pic)
            }
            else
            {
                picasso.load(R.drawable.driverimg).into(User_profile_pic)
            }
        }catch (e:Exception)
        {

        }


        edit_profile_imageview.setOnClickListener {
            val intent=Intent(this,EditProfile::class.java)
            startActivity(intent)
        }
        back_act_view.setOnClickListener {
            onBackPressed()
        }
    }


    private fun user_drtails() {
        showProgressDialog()
        var hashMap = HashMap<String, String>()
        hashMap.put("mobile",phone_number)
        hashMap.put("device_tokanid", "hddhd")

        val SinginCall = APIUtils.getServiceAPI()?.signin(hashMap)
        SinginCall?.enqueue(object : retrofit2.Callback<SigninResponse> {
            override fun onResponse(
                call: Call<SigninResponse>,
                response: Response<SigninResponse>
            ) {
                try {


                    if (response.code() == 200) {
                        if (response.body()?.success.equals("true")) {

                          //  Toast.makeText(this@ViewProfile,response.body()!!.msg,Toast.LENGTH_LONG).show()
                            try {
                                user_gender.setText(response.body()!!.data[0].gender)
                                User_email.setText(response.body()!!.data[0].email)
                                User_name.setText(response.body()!!.data[0].email)
                              var img_url= response.body()!!.data[0].profile_photo
                                if(img_url.isEmpty())
                                {

                                }else
                                {
                                    SharedPreferenceUtils.getInstance(this@ViewProfile)!!.setStringValue(ConstantUtils.Image_Url,img_url)
                                    var picasso=Picasso.get()
                                    picasso.load(response.body()!!.data[0].profile_photo).into(User_profile_pic)
                                }


                            } catch (e:Exception)
                            {

                            }
                            hideProgressDialog()
                        }
                        else {

                            Toast.makeText(this@ViewProfile,response.body()!!.msg,Toast.LENGTH_LONG).show()
                            hideProgressDialog()

                        }
                    }

                } catch (e: Exception) {
                    Log.e("signinrfailuour", e.message.toString())
                    hideProgressDialog()
                }
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                hideProgressDialog()
                Log.e("signinrfailuour", t.message.toString())
                hideProgressDialog()

            }
        })

    }

}
package com.example.taxibookinguserapplication.Main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.Login.LoginActivity
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.LanguageRespnse
import com.example.taxibookinguserapplication.databinding.ActivityLanguageBinding
import com.example.taxibookinguserapplication.util.BaseActivity
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.example.taxibookinguserapplication.util.cont
import com.rehablab.localsaved.shareprefrences
import com.rehablab.util.ConstantUtils
import kotlinx.android.synthetic.main.activity_language.*
import kotlinx.android.synthetic.main.activity_language.tv_next
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_welcome.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LanguageActivity : BaseActivity(), cont {
    lateinit var shrp: shareprefrences

   // lateinit var binding: ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)



        shrp = shareprefrences(this)

        /*binding=DataBindingUtil.setContentView(this,R.layout.activity_language)

        binding.tv_next.setOnClickListener {
            var intent = Intent(this, Welcome_Activity::class.java)
            startActivity(intent)
        }*/


        rel_next.setOnClickListener {
            LanguageSelect()
            /* val intent = Intent(this, Welcome_Activity::class.java)
             startActivity(intent)*/
        }

        onClick()
    }

    private fun LanguageSelect() {
        showProgressDialog()
        var LanguageCallApi = APIUtils.getServiceAPI()?.languagelist()
        LanguageCallApi?.enqueue(object : Callback<LanguageRespnse> {
            override fun onResponse(
                call: Call<LanguageRespnse>,
                response: Response<LanguageRespnse>
            ) {
                try {
                    Log.e("languageresponse", response.body().toString())
                    //   showToastMessage(this@LanguageActivity, response.toString())
                    hideProgressDialog()
                    if (response.code() == 200) {
                        if (response.body()?.success.equals("true")) {
                            if (response.body()!!.data.size > 0) {
                                var data = response.body()!!.data[0]
                                shrp.setStringPreference(ID, data.id)
                                SharedPreferenceUtils.getInstance(this@LanguageActivity)!!.setStringValue(ConstantUtils.Language_Choosen,data.name)
                                val intent =
                                    Intent(this@LanguageActivity, LoginActivity::class.java)
                                intent.putExtra("language", tv_next.text.toString())
                                startActivity(intent)
                                /*    startActivity(Intent(this@LanguageActivity, LoginActivity::class.java)
                                 )*/

                               // showToastMessage(this@LanguageActivity, response.body()?.msg)

                            }
                        } else {
                            showToastMessage(this@LanguageActivity, response.body()?.msg)

                        }
                    }

                } catch (e: Exception) {
                    Log.e("errcdddorr", e.message.toString())
                }
            }

            override fun onFailure(call: Call<LanguageRespnse>, t: Throwable) {
                hideProgressDialog()
                Log.e("languagefailuour", t.message.toString())
                showToastMessage(this@LanguageActivity, t.message.toString())
            }
        })
    }


    private fun onClick() {
        rl_english.setOnClickListener {
            SharedPreferenceUtils.getInstance(this@LanguageActivity)!!.setStringValue(ConstantUtils.Language_Choosen,"English")

            setLanguages()
                img_english.setImageResource(R.drawable.greycircle)
            intent.putExtra("language",tv_english.text.toString())
        }


        rl_french.setOnClickListener {
            setLanguages()
            img_french.setImageResource(R.drawable.greycircle)
            intent.putExtra("language",tv_german.text.toString())
        }
        rl_italian.setOnClickListener {
            setLanguages()
            img_italian.setImageResource(R.drawable.greycircle)
        }
        rl_russion.setOnClickListener {
            setLanguages()
            img_russion.setImageResource(R.drawable.greycircle)
            intent.putExtra("language",tv_russian.text.toString())
        }
        rl_arabic.setOnClickListener {
            setLanguages()
            img_arabic.setImageResource(R.drawable.greycircle)
            intent.putExtra("language",tv_arabic.text.toString())
        }
        rl_spanish.setOnClickListener {
            setLanguages()
            img_spanish.setImageResource(R.drawable.greycircle)
            intent.putExtra("language",tv_spanish.text.toString())
        }
        rl_polish.setOnClickListener {
            setLanguages()
            img_polish.setImageResource(R.drawable.greycircle)
            intent.putExtra("language",tv_polish.text.toString())
        }
    }

    private fun setLanguages() {
        img_english.setImageResource(R.drawable.whitecirle)
        img_french.setImageResource(R.drawable.whitecirle)
        img_italian.setImageResource(R.drawable.whitecirle)
        img_russion.setImageResource(R.drawable.whitecirle)
        img_arabic.setImageResource(R.drawable.whitecirle)
        img_spanish.setImageResource(R.drawable.whitecirle)
        img_polish.setImageResource(R.drawable.whitecirle)


    }

}
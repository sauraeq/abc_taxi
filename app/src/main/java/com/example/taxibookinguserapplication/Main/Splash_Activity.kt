package com.example.taxibookinguserapplication.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.taxibookinguserapplication.LocationMap.Location_fetchActivity
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.rehablab.util.ConstantUtils

class Splash_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({


            var otp_val=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.OTP,"")
            if(otp_val!!.length==4)
            {
                val intent = Intent(this,Location_fetchActivity::class.java)
                startActivity(intent)

            }
            else
            {
                val intent = Intent(this,LanguageActivity::class.java)
                startActivity(intent)

            }



        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}
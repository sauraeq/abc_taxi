package com.example.taxibookinguserapplication.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.taxibookinguserapplication.LocationMap.Location_fetchActivity
import com.example.taxibookinguserapplication.Login.LoginActivity
import com.example.taxibookinguserapplication.Map.Manual_Pick_up
import com.example.taxibookinguserapplication.Map.Pick_up
import com.example.taxibookinguserapplication.Map.Vechicle_list
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.rehablab.util.ConstantUtils

class Splash_Activity : AppCompatActivity() {
    var status=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({

            status=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Activity_Status,"").toString()

            if (status.equals("")) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                when (status!!.toInt()) {
                    0 -> {

                    }
                    1 -> {
                        val intent = Intent(this, Location_fetchActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    2 -> {
                        val intent = Intent(this, Pick_up::class.java)
                        startActivity(intent)
                        finish()

                    }
                    3 -> {
                        val intent = Intent(this, Manual_Pick_up::class.java)
                        startActivity(intent)

                    }
                    4 -> {
                        val intent = Intent(this, Vechicle_list::class.java)
                        startActivity(intent)
                        finish()

                    }
                    5 -> {
                        val intent = Intent(this, TipInformation::class.java)
                        startActivity(intent)
                        finish()

                    }

                }
            }


        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}
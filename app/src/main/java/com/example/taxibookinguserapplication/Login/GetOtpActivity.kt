package com.example.taxibookinguserapplication.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taxibookinguserapplication.R
import kotlinx.android.synthetic.main.activity_get_otp.*

class GetOtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_otp)
        tv_getotp.setOnClickListener {
            val intent=Intent(this,PhoneVerificationActivity::class.java)
            startActivity(intent)
        }
    }
}
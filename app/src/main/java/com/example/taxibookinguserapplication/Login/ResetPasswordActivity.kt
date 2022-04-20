package com.example.taxibookinguserapplication.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taxibookinguserapplication.R
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        tv_cpass.setOnClickListener {
           val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.example.taxibookinguserapplication.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taxibookinguserapplication.Login.LoginActivity
import com.example.taxibookinguserapplication.R
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        rel_getstar.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
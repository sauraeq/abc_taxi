package com.example.taxibookinguserapplication.LocationMap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.taxibookinguserapplication.Map.Manual_Pick_up
import com.example.taxibookinguserapplication.Map.Pick_up
import com.example.taxibookinguserapplication.R
import kotlinx.android.synthetic.main.activity_location_fetch.*


class Location_fetchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_fetch)
        lin_loc.setOnClickListener {
            val intent=Intent(this,Pick_up::class.java)
            startActivity(intent)
        }
        tv_manual_location.setOnClickListener {
            val intent=Intent(this,Manual_Pick_up::class.java)
            startActivity(intent)
        }


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
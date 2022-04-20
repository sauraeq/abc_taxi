package com.example.taxibookinguserapplication.LocationMap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.taxibookinguserapplication.LocationMap.Fragment.Locationfetch3_Fragment
import com.example.taxibookinguserapplication.R

class Locationfetch3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locationfetch3)
        loadFragment(Locationfetch3_Fragment())
       overridePendingTransition(R.anim.slide_right_animation,R.anim.slide_right_animation);
       startActivity(intent)
    }


    private fun loadFragment(Fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framecontainer2,Fragment)
        transaction.commit()
    }
}
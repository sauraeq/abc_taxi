package com.example.taxibookinguserapplication.LocationMap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.taxibookinguserapplication.LocationMap.Fragment.CurrentLocationFragment
import com.example.taxibookinguserapplication.R

class CurrentLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)



        loadFragment(CurrentLocationFragment())
    }

    private fun loadFragment(Fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framecontainer1,Fragment)
        transaction.commit()
    }
}
package com.example.taxibookinguserapplication.LocationMap

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.taxibookinguserapplication.Map.Manual_Pick_up
import com.example.taxibookinguserapplication.Map.Pick_up
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rehablab.util.ConstantUtils
import kotlinx.android.synthetic.main.activity_location_fetch.*


class Location_fetchActivity : AppCompatActivity() {
    var statsu:String="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_fetch)
        SharedPreferenceUtils.getInstance(this)!!.setStringValue(ConstantUtils.Activity_Status,"1")
        getMultiplePermission()



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

    private fun getMultiplePermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report.let {

                        if (report.areAllPermissionsGranted()) {

                            lin_loc.setOnClickListener {
                                val intent=Intent(this@Location_fetchActivity,Pick_up::class.java)
                                startActivity(intent)
                            }

                            tv_manual_location.setOnClickListener {
                                val intent=Intent(this@Location_fetchActivity,Manual_Pick_up::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this@Location_fetchActivity, "Please Grant Permissions to use the app", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener{
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            }.check()

    }

}
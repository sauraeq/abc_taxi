package com.example.taxibookinguserapplication.LocationMap

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taxibookinguserapplication.Map.Manual_Pick_up
import com.example.taxibookinguserapplication.Map.Pick_up
import com.example.taxibookinguserapplication.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_allowpermission.*
import kotlinx.android.synthetic.main.activity_location_fetch.*

class Allowpermission : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allowpermission)
        allow_btn.setOnClickListener {
            getMultiplePermission()
        }
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

                            val intent= Intent(this@Allowpermission, Location_fetchActivity::class.java)
                            startActivity(intent)
                            finish()



                        } else {
                            val intent= Intent(this@Allowpermission, Allowpermission::class.java)
                            startActivity(intent)

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
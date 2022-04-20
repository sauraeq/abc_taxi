package com.example.taxibookinguserapplication.LocationMap

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.example.taxibookinguserapplication.Main.PrivacyPolicyActivity
import com.example.taxibookinguserapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_cancel_trip.*
import kotlinx.android.synthetic.main.canceltrip_layout.*

class CancelTripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_trip)

        popupshow.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
    //    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.canceltrip_layout)


        dialog.findViewById<ImageView>(R.id.img_xmark)!!.setOnClickListener {
            dialog.dismiss()
        }
      dialog.findViewById<TextView>(R.id.tv_canceltrip)!!.setOnClickListener {
            val intent=Intent(this,PrivacyPolicyActivity::class.java)
            startActivity(intent)
           // finishAffinity()
        }

        dialog.show()

    }
}
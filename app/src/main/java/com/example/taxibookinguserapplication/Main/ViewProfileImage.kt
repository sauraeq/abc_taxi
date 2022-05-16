package com.example.taxibookinguserapplication.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taxibookinguserapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_profile_image.*

class ViewProfileImage : AppCompatActivity() {
    var image_url:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile_image)
        image_url= intent.getStringExtra("image_url").toString()

        var picasso=Picasso.get()
        if (image_url.equals(""))
        {
            picasso.load(R.drawable.driverimg).into(profile_img)
        }
        else
        {
            picasso.load(image_url).into(profile_img)
        }
        img_cancelmark.setOnClickListener{
            onBackPressed()
        }

    }
}
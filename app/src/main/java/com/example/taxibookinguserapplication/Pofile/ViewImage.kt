package com.example.taxibookinguserapplication.Pofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taxibookinguserapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_image.*

class ViewImage : AppCompatActivity() {
    var Iamge_url:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_image)
        Iamge_url= intent.getStringExtra("image_url").toString()
        if (Iamge_url.equals(""))
        {
            val picasso=Picasso.get()
            picasso.load(R.drawable.driverimg).into(image_view_viewactivity)
        }
        else
        {
            val picasso=Picasso.get()
            picasso.load(Iamge_url).into(image_view_viewactivity)
        }
       // onBackPressed()


    }
}
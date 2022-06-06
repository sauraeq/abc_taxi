package com.example.taxibookinguserapplication.Main.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.PrivacyResData
import com.example.taxibookinguserapplication.Responses.TripHistoryData
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class TripHistory_adapter (var mContext: Context, var mlist: List<TripHistoryData>) : RecyclerView.Adapter<TripHistory_adapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        lateinit var time_date: TextView
        lateinit var price: TextView
        lateinit var driver_img: CircleImageView
        lateinit var distance:TextView
        lateinit var pick_locatio:TextView
        lateinit var drop_location:TextView
        lateinit var driver_name:TextView
        lateinit var rating_trip_histryy:TextView

        init {

            time_date=itemView.findViewById(R.id.trip_time)
            price=itemView.findViewById(R.id.trip_amount)
            driver_name=itemView.findViewById(R.id.trip_name)
            driver_img=itemView.findViewById(R.id.trip_image)
            pick_locatio=itemView.findViewById(R.id.trip_pick)
            drop_location=itemView.findViewById(R.id.trip_drop)
            distance=itemView.findViewById(R.id.trip_distance)
            rating_trip_histryy=itemView.findViewById(R.id.rating_trip_histry)


        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var  aContext: Context
        val v= LayoutInflater.from(parent.context).inflate(R.layout.trip_history_adapter_layout,parent,false)
        aContext=parent.context

        return ViewHolder(v)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.price.setText(mlist[position].amount.toString())
        //holder.time_date.setText(mlist[position].created_date)
      //  holder.vehicle.setText(mlist[position].driver_name)
        holder.price.text= "CHF"+" "+mlist[position].amount as CharSequence?
        holder.time_date.text= mlist[position].created_date
        var username=mlist[position].driver_name
        holder.driver_name.text= getCapsSentences(username)
        holder.pick_locatio.text= mlist[position].pickup_address
        holder.drop_location.text= mlist[position].drop_address
        holder.distance.text= mlist[position].distance+" "+"Km"
        holder.rating_trip_histryy.text="⭐"+" "+"4"

        if (mlist[position].driver_photo.isEmpty())
        {
            var pica= Picasso.get()
            pica.load(R.drawable.driverimg).into(holder.driver_img)
        }
        else
        {
            var pica= Picasso.get()
            pica.load(mlist[position].driver_photo).into(holder.driver_img)
        }


     /*   holder.description.text=mlist[position].description
        holder.introduction.text=mlist[position].id*/
    }

    override fun getItemCount(): Int {

        return mlist.size
    }

    private fun getCapsSentences(tagName: String): String? {
        val splits = tagName.lowercase(Locale.getDefault()).split(" ".toRegex()).toTypedArray()
        val sb = StringBuilder()
        try {
            for (i in splits.indices) {
                val eachWord = splits[i]
                if (i > 0 && eachWord.length > 0) {
                    sb.append(" ")
                }
                val cap = (eachWord.substring(0, 1).uppercase(Locale.getDefault())
                        + eachWord.substring(1))
                sb.append(cap)
            }
        } catch (e:Exception)
        {

        }

        return sb.toString()
    }

}
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

class TripHistory_adapter (var mContext: Context, var mlist: List<TripHistoryData>) : RecyclerView.Adapter<TripHistory_adapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        lateinit var time_date: TextView
        lateinit var price: TextView
        lateinit var vehicle: TextView


        init {

            time_date=itemView.findViewById(R.id.trip_time)
            price=itemView.findViewById(R.id.trip_fare)
            vehicle=itemView.findViewById(R.id.trip_vehicle)


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
        holder.vehicle.text= mlist[position].driver_name


     /*   holder.description.text=mlist[position].description
        holder.introduction.text=mlist[position].id*/
    }

    override fun getItemCount(): Int {

        return mlist.size
    }

}
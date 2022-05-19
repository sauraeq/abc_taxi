package com.example.taxibookinguserapplication.Main.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.CancelRideReasonResponseData

class CancelTripAdapter (
    var mContext: Context, val lintener: PractiseInterface,
    var mlist: List<CancelRideReasonResponseData>)  :
    RecyclerView
    .Adapter<CancelTripAdapter.ViewHolder>() {
    private var selectedItemPosition: String = ""
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var Reason: TextView
        lateinit var linear_trip:CardView
        private var selectedItemPosition: String = ""


        init {
            Reason = itemView.findViewById(R.id.reason_cancel_trip)
            linear_trip=itemView.findViewById(R.id.trip_linear_layout)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var aContext: Context
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cancel_trip_layout_adapter, parent, false)
        aContext = parent.context

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.Reason.text = mlist[position].description
        holder.itemView.setOnClickListener {

            selectedItemPosition=position.toString()
            notifyDataSetChanged()
            lintener.onclick(mlist[position].description)

        }
        if(selectedItemPosition==position.toString())
        {
            holder.Reason.setTextColor(Color.BLACK)
            holder.linear_trip.setBackgroundColor(Color.GRAY)
        }
        else
        {   holder.Reason.setTextColor(Color.BLACK)
            holder.linear_trip.setBackgroundColor(Color.WHITE)
        }

    }

    override fun getItemCount(): Int {

        return mlist.size
    }

    interface PractiseInterface {
        fun onclick(name: String)
    }
}

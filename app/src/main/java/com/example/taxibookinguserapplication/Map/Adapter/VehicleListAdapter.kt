package com.example.taxibookinguserapplication.Map.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.Main.Adapter.TripHistory_adapter
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.TripHistoryData
import com.example.taxibookinguserapplication.Responses.VehicleResponseData
import com.squareup.picasso.Picasso

class VehicleListAdapter (var mContext: Context,val lintener: PractiseInterface, var mlist: List<VehicleResponseData>) : RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {
    private var selectedItemPosition: String = ""
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        lateinit var image_veh: ImageView
        lateinit var car_category: TextView
        lateinit var car_description: TextView
        lateinit var price: TextView
        lateinit var car_list_layout:LinearLayout



        init {

            image_veh=itemView.findViewById(R.id.image_veh)
            car_category=itemView.findViewById(R.id.veh_category_txt)
            car_description=itemView.findViewById(R.id.veh_description_txt)
            price=itemView.findViewById(R.id.toatal_fare_txtview)
            car_list_layout=itemView.findViewById(R.id.carlist_layout)



        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var  aContext: Context
        val v= LayoutInflater.from(parent.context).inflate(R.layout.vehicle_list_adapter_lyt,parent,false)
        aContext=parent.context

        return ViewHolder(v)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      var vehList=mlist[position].image

       // holder.image_veh.text= mlist[position].amount as CharSequence?
        if (vehList.isNotEmpty())
        {
            var picasso = Picasso.get()
            picasso.load(R.drawable.nanocarimg).into(holder.image_veh)
        }
        else{
            var picasso = Picasso.get()
            picasso.load(R.drawable.nanocarimg).into(holder.image_veh)
        }

        holder.car_category.text= mlist[position].vehicle_no
        holder.car_description.text= mlist[position].vehicle_name
        holder.price.text= "CHF"+mlist[position].amount

        holder.itemView.setOnClickListener {
            selectedItemPosition=position.toString()
            notifyDataSetChanged()
            lintener.onclick( mlist[position].driver_id)
            holder.car_list_layout.setBackgroundColor(Color.WHITE)

        }
    if(selectedItemPosition==position.toString())
    {

        holder.car_list_layout.setBackgroundColor(Color.parseColor("#F3F3F3"))
    }
        else
    {
        holder.car_list_layout.setBackgroundColor(Color.WHITE)
    }


    }



    override fun getItemCount(): Int {

        return mlist.size
    }

    interface PractiseInterface{
        fun onclick(driver_id:String)
    }


}
package com.example.taxibookinguserapplication.Main.Adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.PrivacyResData
import com.example.taxibookinguserapplication.Responses.TCResData

class TCAdapter (var mContext: Context, var mlist: List<TCResData>) : RecyclerView.Adapter<TCAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        ///   lateinit var nameof: TextView
        // lateinit var time: TextView

        lateinit var introduction: TextView
        lateinit var description: TextView


        init {
            // nameof=itemView.findViewById(R.id.notification_description)
            // time=itemView.findViewById(R.id.notification_time)

            introduction=itemView.findViewById(R.id.terms_heading)
            description=itemView.findViewById(R.id.terms_description)


        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var  aContext: Context
        val v= LayoutInflater.from(parent.context).inflate(R.layout.tc_adapter_lyt,parent,false)
        aContext=parent.context

        return ViewHolder(v)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  holder.nameof.text=notiData[position].description
        //holder.time.text=notiData[position].time

        holder.description.text= Html.fromHtml(mlist[position].description)
        holder.introduction.text=mlist[position].id
    }

    override fun getItemCount(): Int {

        return mlist.size
    }

}
package com.example.carrentalgr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.json.JSONObject

class CarEventAdapter(
    var carEvent : Map<Int,JSONObject>,
    var context: Context) : RecyclerView.Adapter<CarEventAdapter.CarEventViewHolder>(){

    class CarEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var textViewType : TextView = itemView.findViewById(R.id.type_Card)
        var textViewID : TextView = itemView.findViewById(R.id.id_Card)
        var textViewTime : TextView = itemView.findViewById(R.id.timestamp_Card)
        var textViewComment : TextView = itemView.findViewById(R.id.comment_Card)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarEventViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.card_design, parent, false)
        return CarEventViewHolder(view)
    }

    override fun getItemCount(): Int {

        return carEvent.size

    }

    override fun onBindViewHolder(holder: CarEventViewHolder, position: Int) {

        holder.textViewType.text = carEvent.get(position)?.getString("type")
        holder.textViewID.text = carEvent.get(position)?.getString("id")
        holder.textViewTime.text = carEvent.get(position)?.getString("timestamp")
        holder.textViewComment.text = carEvent.get(position)?.getString("comments")

    }

}
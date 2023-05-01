package com.example.carrentalgr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class SummaryEventAdapter(
    var summaryMap: Map<Int, JSONObject>,
    var context: Context) : RecyclerView.Adapter<SummaryEventAdapter.SummaryViewHolder>(){

    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var summaryID : TextView = itemView.findViewById(R.id.id_summary)
        var summaryStartTime : TextView = itemView.findViewById(R.id.start_timestamp_summary)
        var summaryEndTime : TextView = itemView.findViewById(R.id.end_timestamp_summary)
        var summaryDuration : TextView = itemView.findViewById(R.id.session_duration_summary)
        var summarySession : TextView = itemView.findViewById(R.id.session_expired_summary)
        var summaryDamage : TextView = itemView.findViewById(R.id.car_damaged_summary)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.summary_report,parent,false)
        return SummaryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return summaryMap.size
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.summaryID.text = summaryMap.get(position)?.getString("id")
        holder.summaryStartTime.text = summaryMap.get(position)?.getString("start_timestamp")
        holder.summaryEndTime.text = summaryMap.get(position)?.getString("end_timestamp")
        holder.summaryDuration.text = summaryMap.get(position)?.getString("session_duration")
        holder.summarySession.text = "${summaryMap.get(position)?.getString("session_expired")}"
        holder.summaryDamage.text = "${summaryMap.get(position)?.getString("car_damaged")}"
    }

}
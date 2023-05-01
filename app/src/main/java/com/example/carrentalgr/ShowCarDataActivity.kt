package com.example.carrentalgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.carrentalgr.database.DatabaseHelper

class ShowCarDataActivity : AppCompatActivity() {

    lateinit var summaryRecyclerView : RecyclerView
    lateinit var summaryAdapter : SummaryEventAdapter

    lateinit var dbHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_car_data)

        summaryRecyclerView = findViewById(R.id.summaryRView)
        summaryRecyclerView.layoutManager = LinearLayoutManager(this@ShowCarDataActivity)

        dbHelper = DatabaseHelper(this@ShowCarDataActivity)
        var data = dbHelper.getSessionSummary()

        summaryAdapter = SummaryEventAdapter(data,this@ShowCarDataActivity)

        summaryRecyclerView.adapter = summaryAdapter

    }
}
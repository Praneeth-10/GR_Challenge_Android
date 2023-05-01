package com.example.carrentalgr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrentalgr.database.DatabaseHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    lateinit var startRecyclerView: RecyclerView
    lateinit var endRecyclerView: RecyclerView

    lateinit var expButton : Button
    lateinit var nextButton :Button

    var startMap = mutableMapOf<Int, JSONObject>()
    var endMap = mutableMapOf<Int, JSONObject>()

    lateinit var startAdapter: CarEventAdapter
    lateinit var endAdapter: CarEventAdapter

    lateinit var my_layout : ConstraintLayout
    lateinit var insertStart : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        insertStart = DatabaseHelper(this@MainActivity)

        startRecyclerView = findViewById(R.id.startRView)
        endRecyclerView = findViewById(R.id.endRView)

        startRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        endRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        loadJSONData()

        startAdapter = CarEventAdapter(startMap,this@MainActivity)
        endAdapter = CarEventAdapter(endMap,this@MainActivity)

        startRecyclerView.adapter = startAdapter
        endRecyclerView.adapter = endAdapter

        expButton = findViewById(R.id.expBtn)
        nextButton = findViewById(R.id.buttonTemp)
        my_layout = findViewById(R.id.main_layout)

        nextButton.setOnClickListener {
            var int = Intent(this@MainActivity,ShowCarDataActivity::class.java)
            startActivity(int)
        }

        expButton.setOnClickListener {
            val scope = CoroutineScope(Job() + Dispatchers.Main)

            scope.launch {
                Snackbar.make(my_layout,"Exporting Start Type Data!",Snackbar.LENGTH_SHORT).setAction("Close",
                View.OnClickListener {  }).show()

                var check = insertStart.startSession(startMap,this@MainActivity)

                delay(2000)

                if(check){
                    Snackbar.make(my_layout,"Starting End Type Export",Snackbar.LENGTH_SHORT).setAction("Close",
                        View.OnClickListener {  }).show()

                    var flag = insertStart.updateStartSession(endMap,this@MainActivity)
                    delay(2000)
                    if(flag){
                        nextButton.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun loadJSONData(){
        var inputStream : InputStream? = null
        var jsonString : String


        try {
            inputStream = assets.open("events.json")
            jsonString = inputStream.bufferedReader().use { it.readText() }

            var jsonArr = JSONArray(jsonString)
            var k : Int = 0
            var j : Int = 0

            for(i in 0 until jsonArr.length()){
                var jsonObject = jsonArr.getJSONObject(i)
                if(jsonObject.getString("type") == "START")
                    startMap.put(k++, jsonObject)
                else if (jsonObject.getString("type") == "END")
                    endMap.put(j++, jsonObject)
                else
                    continue
            }
        }
        catch (exception : Exception){
            exception.printStackTrace()
        }
        finally {
            inputStream?.close()
        }
    }

}
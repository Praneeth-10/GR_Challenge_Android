package com.example.carrentalgr.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.lang.UCharacter.IndicPositionalCategory.NA
import android.util.Log
import android.widget.Toast
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.*
import org.json.JSONObject

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_Name, null, DB_version) {

    companion object{
        private val DB_Name = "summary_Report"
        private val DB_version = 1
        private val TABLE_NAME = "car_Report"
        private val ID = "id"
        private val TYPE = "type"
        private val START_COMMENTS = "start_comments"
        private val END_COMMENTS = "end_comments"
        private val START_TIMESTAMP = "start_timestamp"
        private val END_TIMESTAMP = "end_timestamp"
        private val DURATION = "session_duration"
        private val RETURNED = "session_expired"
        private val DAMAGED = "car_damaged"
    }

    override fun onCreate(dbHelper: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID TEXT UNIQUE PRIMARY KEY, $TYPE TEXT," +
                "$START_TIMESTAMP TEXT, $END_TIMESTAMP TEXT, $START_COMMENTS TEXT," +
                "$END_COMMENTS TEXT, $DURATION INTEGER, $RETURNED TEXT, $DAMAGED TEXT);"
        dbHelper?.execSQL(CREATE_TABLE)

    }

    override fun onUpgrade(dbHelper: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        dbHelper?.execSQL(DROP_TABLE)
        onCreate(dbHelper)
    }

    // insert Start JSON Data
    fun startSession(map: Map<Int,JSONObject>, context: Context) : Boolean{
        truncTable()
        val db = this.writableDatabase
        var check : Long = 0
        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            for ( i in 0 until map.size) {
                val values = ContentValues()
                values.put(ID, map[i]?.getString("id").toString())
                values.put(TYPE, map[i]?.getString("type").toString())
                values.put(START_TIMESTAMP, map[i]?.getString("timestamp").toString())
                values.put(START_COMMENTS, map[i]?.getString("comments").toString())

                withContext(Dispatchers.IO){
                    check = db.insert(TABLE_NAME,null,values)
                }
                delay(10)
            }
            db.close()
            withContext(Dispatchers.Main){
                Toast.makeText(context,"New Start Data is inserted!",Toast.LENGTH_LONG).show()
            }
            delay(2000)
        }
        return (Integer.parseInt("$check") != -1)
    }

    // inserting end Sessions
    fun updateStartSession(map: Map<Int, JSONObject>,context: Context) : Boolean{

        var flag : Long = 0
        val db = this.writableDatabase

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            for ( i in 0 until map.size) {
                val selectQuery = "SELECT $START_TIMESTAMP FROM $TABLE_NAME WHERE $ID = '${map[i]?.getString("id")}'"
                val cursor = db.rawQuery(selectQuery,null)
                cursor?.moveToFirst()

                val strTime = cursor.getInt(cursor.getColumnIndexOrThrow(START_TIMESTAMP))
                val duration : Int = map[i]?.getString("timestamp")?.toInt()!! - strTime
                val exactDuration = "${duration/3600}:${(duration%3600)/60}:${(duration%3600)%60}"

                val values = ContentValues()

                withContext(Dispatchers.IO){
                    values.put(TYPE, map[i]?.getString("type"))
                    values.put(END_TIMESTAMP, map[i]?.getString("timestamp"))
                    values.put(END_COMMENTS, map[i]?.getString("comments"))
                    values.put(DURATION, exactDuration)
                    if(duration/3600 >= 24)
                        values.put(RETURNED,"true")
                    else
                        values.put(RETURNED,"false")

                    if(map[i]?.getString("comments") != "")
                        values.put(DAMAGED,"true")
                    else
                        values.put(DAMAGED,"false")

                    flag = db.update(TABLE_NAME,values,"$ID=?", arrayOf(map[i]?.getString("id"))).toLong()
                    cursor.close()
                }
                delay(20)
            }
            db.close()
            withContext(Dispatchers.Main){
                Toast.makeText(context,"New End Data is inserted!",Toast.LENGTH_LONG).show()
            }
            delay(2000)
        }

        return (Integer.parseInt("$flag") != -1)
    }

    //Selecting all sessions
    fun getSessionSummary() : Map<Int,JSONObject>{
        var map = mutableMapOf<Int,JSONObject>()
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery,null)
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                var k = 0
                do {
                    val id = cursor.getStringOrNull(cursor.getColumnIndex(ID))
                    if(id.isNullOrEmpty())
                        break

                    val jsonObject = JSONObject()
                    jsonObject.put("id", id)
                    jsonObject.put("start_timestamp", cursor.getStringOrNull(cursor.getColumnIndex(START_TIMESTAMP)))
                    jsonObject.put("end_timestamp", cursor.getStringOrNull(cursor.getColumnIndex(END_TIMESTAMP))?: NA)
                    jsonObject.put("session_duration", cursor.getStringOrNull(cursor.getColumnIndex(DURATION))?: NA)
                    jsonObject.put("session_expired", cursor.getStringOrNull(cursor.getColumnIndex(RETURNED))?: "false")
                    jsonObject.put("car_damaged", cursor.getStringOrNull(cursor.getColumnIndex(DAMAGED))?: "false")
                    map.put(k++,jsonObject)
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return map
    }

    fun truncTable() : Boolean{
        val db = this.writableDatabase
        val selectQuery = "DELETE FROM $TABLE_NAME"
        return try {
            db.execSQL(selectQuery)
            true
        }catch (exception : Exception){
            Log.e("Message",exception.printStackTrace().toString())
            false
        }
        finally {
            db.close()
        }

    }


}
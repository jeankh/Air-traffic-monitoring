package com.example.airtrafficmonitoring.activiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.airtrafficmonitoring.R
import com.example.airtrafficmonitoring.Utils

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val airportList = Utils.generateAirportList()
        val spinner = findViewById<Spinner>(R.id.airport_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item , airportList)
        spinner.adapter = adapter

//        findViewById<TextView>(R.id.from_date_view).setOnClickListener {
//            showDatePickerDialog()
//        }
//
//
//
//     fun showDatePickerDialog() {
//        val dateSetListener = OnDateSetListener {view, year, monthOfYear, dayOfMonth ->
//            val calendar = Calendar.getInstance()
//            calendar.set(Calendar.YEAR, year)
//            calendar.set(Calendar.MONTH, monthOfYear)
//            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            findViewById<TextView>(R.id.from_date_view).text= Utils.dateToString(calendar.time)
//        }
//
//        val calendar = Calendar.getInstance()
//        val datePickerDialog = DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
//         datePickerDialog.show()
//
//        }
//
//    }



    }
}
package com.example.airtrafficmonitoring.activiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.airtrafficmonitoring.Airport
import com.example.airtrafficmonitoring.R
import com.example.airtrafficmonitoring.Utils
import com.example.airtrafficmonitoring.ViewModels.HomeViewModel

class Home : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Log.i("SERGIO","ici")

        // Generate the airport list
        val airportList = Utils.generateAirportList()
        val spinner = findViewById<Spinner>(R.id.airport_spinner)
        val adapter = ArrayAdapter<Airport>(this, android.R.layout.simple_spinner_dropdown_item, airportList)
        spinner.adapter = adapter
//
//        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
//
//
//
//        // Set the airport list in the ViewModel()
//        homeViewModel.setAirportList(airportList)
//
//        // Observe changes to the airport list in the ViewModel
//        homeViewModel.getAirportList().observe(this) { updatedAirportList ->
//            // Update the adapter with the new data
//            adapter.clear()
//            adapter.addAll(updatedAirportList)
//        }
    }
}
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

//
//
//    }
//}
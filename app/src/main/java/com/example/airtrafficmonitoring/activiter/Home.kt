package com.example.airtrafficmonitoring.activiter

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.airtrafficmonitoring.Airport
import com.example.airtrafficmonitoring.R
import com.example.airtrafficmonitoring.RequestManager
import com.example.airtrafficmonitoring.Utils
import com.example.airtrafficmonitoring.ViewModels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class Home : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel

    private lateinit var beginDateLabel: TextView
    private lateinit var endDateLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        beginDateLabel = findViewById<TextView>(R.id.from_date)
        endDateLabel = findViewById<TextView>(R.id.to_date)

        beginDateLabel.setOnClickListener { showDatePickerDialog(HomeViewModel.DateType.BEGIN) }
        endDateLabel.setOnClickListener { showDatePickerDialog(HomeViewModel.DateType.END) }

        val airportSpinner = findViewById<Spinner>(R.id.airport_spinner)

        viewModel.getAirportNamesListLiveData().observe(this) {
            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item, it
            )

            airportSpinner.adapter = adapter
        }


        viewModel.getBeginDateLiveData().observe(this) {
            beginDateLabel.text = Utils.dateToString(it.time)
        }

        viewModel.getEndDateLiveData().observe(this) {
            endDateLabel.text = Utils.dateToString(it.time)
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            // Récupérer données pour la requête
            // Date de début
            val begin = viewModel.getBeginDateLiveData().value!!.timeInMillis / 1000
            // Date de fin
            val end = viewModel.getEndDateLiveData().value!!.timeInMillis / 1000
            // Airport
            val selectedAirportIndex = airportSpinner.selectedItemPosition
            val airport = viewModel.getAirportListLiveData().value!![selectedAirportIndex]
            val icao = airport.icao
            // Depart ou arrivée
            val isArrival = findViewById<Switch>(R.id.airport_switch).isChecked


            // Ouvrir une nouvelle activité avec les infos de la requête

            val intent = Intent(this, FlightListActivity::class.java)

            intent.putExtra("BEGIN",begin)
            intent.putExtra("END",end)
            intent.putExtra("IS_ARRIVAL",isArrival)
            intent.putExtra("ICAO",icao)

            startActivity(intent)
        }


    }

    // open date picker dialog
    private fun showDatePickerDialog(dateType: HomeViewModel.DateType) {
        // Date Select Listener.
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.updateCalendarLiveData(dateType, calendar)
            }

        val currentCalendar = if (dateType == HomeViewModel.DateType.BEGIN) viewModel.getBeginDateLiveData().value else viewModel.getEndDateLiveData().value

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            currentCalendar!!.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH),
            currentCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
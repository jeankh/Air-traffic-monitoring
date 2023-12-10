package com.example.airtrafficmonitoring

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class FlightInfoCell : LinearLayout {

    private lateinit var depDateTextView: TextView
    private lateinit var depAirportTextView: TextView
    private lateinit var depHourTextView: TextView
    private lateinit var arrDateTextView: TextView
    private lateinit var arrAirportTextView: TextView
    private lateinit var arrHourTextView: TextView
    private lateinit var flightDurationTextView: TextView
    private lateinit var flightNameTextView: TextView


    constructor(context: Context?) : super(context) {
        initLayout()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initLayout()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initLayout()
    }

    private fun bindViews() {
        // Bind views by finding their IDs
        depDateTextView = findViewById(R.id.depDateTextView)
        depAirportTextView = findViewById(R.id.depAirportTextView)
        depHourTextView = findViewById(R.id.depTimeTextView)
        arrDateTextView = findViewById(R.id.arrDateTextView)
        arrAirportTextView = findViewById(R.id.arrAirportTextView)
        arrHourTextView = findViewById(R.id.arrTimeTextView)
        flightDurationTextView = findViewById(R.id.flightDurationTextView)
        flightNameTextView = findViewById(R.id.flightNumberTextView)
    }

    fun bindData(flight: FlightModel) {
        Log.d("TAG", "message")
        // Fill your views
        depDateTextView.text = formatDate(flight.firstSeen)
        depHourTextView.text = formatTime(flight.firstSeen)
        depAirportTextView.text = flight.estDepartureAirport
        flightNameTextView.text = flight.callsign
        flightDurationTextView.text = calculateTravelTime(flight.firstSeen, flight.lastSeen)
        arrDateTextView.text = formatDate(flight.lastSeen)
        arrHourTextView.text = formatTime(flight.lastSeen)
        arrAirportTextView.text = flight.estArrivalAirport
    }

    // Function to format timestamp to date
    private fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000 // converting seconds to milliseconds
        return formatter.format(calendar.time)
    }

    // Function to format timestamp to time
    private fun formatTime(timestamp: Long): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000 // converting seconds to milliseconds
        return formatter.format(calendar.time)
    }

    // Function to calculate travel time in hours and minutes
    private fun calculateTravelTime(departureTime: Long, arrivalTime: Long): String {
        val travelTimeInMillis = (arrivalTime - departureTime) * 1000 // converting seconds to milliseconds
        val hours = travelTimeInMillis / (1000 * 60 * 60)
        val minutes = (travelTimeInMillis % (1000 * 60 * 60)) / (1000 * 60)
        return String.format(Locale.getDefault(), "%dh %02d", hours, minutes)
    }

    private fun initLayout() {
        LayoutInflater.from(context).inflate(R.layout.flight_cell, this, true)
        bindViews()
    }

}
package com.example.airtrafficmonitoring

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FlightAdapter(private val flightList: List<FlightData3>) : RecyclerView.Adapter<FlightAdapter.FlightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flight_item, parent, false)
        return FlightViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flightList[position]
        holder.bind(flight)
    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    inner class FlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val departureAirportTextView: TextView = itemView.findViewById(R.id.departureAirportTextView)
        private val arrivalAirportTextView: TextView = itemView.findViewById(R.id.arrivalAirportTextView)
        private val departureDateTimeTextView: TextView = itemView.findViewById(R.id.departureDateTimeTextView)
        private val arrivalDateTimeTextView: TextView = itemView.findViewById(R.id.arrivalDateTimeTextView)
        private val flightDurationTextView: TextView = itemView.findViewById(R.id.flightDurationTextView)
        private val flightNameTextView: TextView = itemView.findViewById(R.id.flightNameTextView)

        fun bind(flight: FlightData3) {
            departureAirportTextView.text = "Départ: " + flight.departureAirport
            arrivalAirportTextView.text = "Arrivée: " + flight.arrivalAirport
            departureDateTimeTextView.text = "Date de départ: " + flight.departureDateTime
            arrivalDateTimeTextView.text = "Date d'arrivée: " + flight.arrivalDateTime
            flightDurationTextView.text = "Temps de vol: " + flight.flightDuration
            flightNameTextView.text = "Nom du vol: " + flight.flightName
        }
    }
}

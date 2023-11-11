 package com.example.airtrafficmonitoring.activiter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.airtrafficmonitoring.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airtrafficmonitoring.FlightAdapter
import com.example.airtrafficmonitoring.FlightData
import com.example.airtrafficmonitoring.FlightData3
import com.example.airtrafficmonitoring.ViewModels.HomeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

 //mettre le time 0 dans un bouton sur la meme interface
 class Detail3jour : AppCompatActivity() {

     private lateinit var viewModel: HomeViewModel
     private lateinit var recyclerView: RecyclerView
     private val flightList = mutableListOf<FlightData3>()

     @SuppressLint("MissingInflatedId")
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_detail3jour)
         viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
         //recyclerview
         recyclerView = findViewById(R.id.flightRecyclerView)
         recyclerView.layoutManager = LinearLayoutManager(this)
         recyclerView.adapter = FlightAdapter(flightList)

         val progressText =findViewById<TextView>(R.id.progressText)
         //connexion internet
         val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         val networkInfo = connectivityManager.activeNetworkInfo
         if (networkInfo != null && networkInfo.isConnected){
             GlobalScope.launch(Dispatchers.IO) {
                 fetchDataFromAPI()
             }
         } else {
             // Pas de connexion Internet, affichez un message d'erreur ou effectuez une action appropriée
             runOnUiThread {
                 progressText.text = "Pas de connexion Internet"
             }}
         val btnBack = findViewById<Button>(R.id.btnBack)
         btnBack.setOnClickListener {
             onBackPressed()
         }
     }

     private fun fetchDataFromAPI() {
         //recuperation des donnée de l'ex-activity
         var intent: Intent? = getIntent()
         var numavion = intent!!.getStringExtra("icao24")
         //calcul des time pour 3jours
         val calendarNow = Calendar.getInstance()
         val timestampNow = calendarNow.timeInMillis / 1000
         val progressBar = findViewById<ProgressBar>(R.id.progressBar)
         val progressText =findViewById<TextView>(R.id.progressText)

         val calendarThreeDaysAgo = Calendar.getInstance()
         calendarThreeDaysAgo.add(Calendar.DAY_OF_YEAR, -3)
         val timestampThreeDaysAgo = calendarThreeDaysAgo.timeInMillis / 1000
         val urlStr = "https://opensky-network.org/api/flights/aircraft?icao24=$numavion&begin=$timestampThreeDaysAgo&end=$timestampNow"
             val url = URL(urlStr)
             val connection = url.openConnection() as HttpURLConnection
             connection.requestMethod = "GET"
             val responseCode = connection.responseCode
            //reponse de API
             if (responseCode == HttpURLConnection.HTTP_OK) {
                 val inputStream = connection.inputStream
                 val reader = BufferedReader(InputStreamReader(inputStream))
                 val response = StringBuilder()
                 var line: String?
                 while (reader.readLine().also { line = it } != null) {
                     response.append(line)
                 }
                 val gson = Gson()
                 val listType = object : TypeToken<List<FlightData>>() {}.type
                 val flightDataList = gson.fromJson<List<FlightData>>(response.toString(), listType)
                 val newFlightList = flightDataList .map { flightInfo ->
                     FlightData3(
                         flightInfo.estDepartureAirport ?: "Aéroport inconnu",
                         flightInfo.estArrivalAirport ?: "Aéroport inconnu",
                         flightInfo.firstSeen.toString(),
                         flightInfo.lastSeen.toString(),
                         "2 heures",
                         flightInfo.callsign
                     )
                 }
                 flightList.addAll(newFlightList)
                 // Mettez à jour le TextView avec la réponse de l'API sur le thread principal.
                 runOnUiThread {
                     recyclerView.adapter = FlightAdapter(flightList)
                     progressBar.visibility = View.GONE
                     progressText.visibility = View.GONE

                 }
             }
             else{
                 runOnUiThread {
                     progressText.text="pas de signal"
                 }
             }
     }
 }


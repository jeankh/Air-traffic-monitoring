 package com.example.airtrafficmonitoring.activiter

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
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
import android.widget.ArrayAdapter
import android.widget.Button
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

 class Detail3jour : AppCompatActivity() {
     private lateinit var apiResponseTextView: TextView // Ajoutez cette ligne
     private lateinit var viewModel: HomeViewModel
     private lateinit var recyclerView: RecyclerView
     private val flightList = mutableListOf<FlightData3>()

     @SuppressLint("MissingInflatedId")
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_detail3jour)

         apiResponseTextView = findViewById(R.id.apiResponseTextView) // Récupérez la référence du TextView


//---------------------------------------------------------*
         viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


         recyclerView = findViewById(R.id.flightRecyclerView)
         recyclerView.layoutManager = LinearLayoutManager(this)
         recyclerView.adapter = FlightAdapter(flightList)
         //---------------------------------------
         // Exécutez la requête sur un thread d'arrière-plan (utilisation de Kotlin Coroutines).
         GlobalScope.launch(Dispatchers.IO) {
             fetchDataFromAPI()
         }
         val btnBack = findViewById<Button>(R.id.btnBack)

         btnBack.setOnClickListener {
             onBackPressed()
         }
     }

     private fun fetchDataFromAPI() {
         var intent: Intent? = getIntent()
         var numavion = intent!!.getStringExtra("icao24")
         val calendarNow = Calendar.getInstance()
         val timestampNow = calendarNow.timeInMillis / 1000

         val calendarThreeDaysAgo = Calendar.getInstance()
         calendarThreeDaysAgo.add(Calendar.DAY_OF_YEAR, -3)
         val timestampThreeDaysAgo = calendarThreeDaysAgo.timeInMillis / 1000

         Log.d("Timestamp", "Timestamp actuel : $timestampNow")
         Log.d("Timestamp", "Timestamp il y a trois jours : $timestampThreeDaysAgo")
         val urlStr = "https://opensky-network.org/api/flights/aircraft?icao24=$numavion&begin=1695810322&end=1695983276"

         try {
             val url = URL(urlStr)
             val connection = url.openConnection() as HttpURLConnection

             connection.requestMethod = "GET"
             val responseCode = connection.responseCode
             Log.d("33jour", responseCode.toString())
             if (responseCode == HttpURLConnection.HTTP_OK) {
                 val inputStream = connection.inputStream
                 val reader = BufferedReader(InputStreamReader(inputStream))
                 val response = StringBuilder()
                 var line: String?

                 while (reader.readLine().also { line = it } != null) {
                     response.append(line)
                 }
                 Log.d("titi", "totototo ")
                 val gson = Gson()
                 val listType = object : TypeToken<List<FlightData>>() {}.type
                 val flightDataList = gson.fromJson<List<FlightData>>(response.toString(), listType)
                 Log.d("reponsetoto", flightDataList.toString())
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

                     apiResponseTextView.text ="update"
                 }
             } else {
                 Log.d("reponse", "erreur if ")
             }
         } catch (e: Exception) {
             Log.d("reponse", "erreur try")
         }
     }
 }


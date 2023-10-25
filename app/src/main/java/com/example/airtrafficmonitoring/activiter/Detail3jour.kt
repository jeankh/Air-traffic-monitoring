 package com.example.airtrafficmonitoring.activiter

import android.annotation.SuppressLint
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
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airtrafficmonitoring.FlightAdapter
import com.example.airtrafficmonitoring.FlightData3
import com.example.airtrafficmonitoring.ViewModels.HomeViewModel

 class Detail3jour : AppCompatActivity() {
     private lateinit var apiResponseTextView: TextView // Ajoutez cette ligne
     private lateinit var viewModel: HomeViewModel
     private lateinit var recyclerView: RecyclerView
     @SuppressLint("MissingInflatedId")
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_detail3jour)

         apiResponseTextView = findViewById(R.id.apiResponseTextView) // Récupérez la référence du TextView

//---------------------------------------------------------*
         viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
         val flightList = listOf(
             FlightData3(
                 "Aéroport de départ 1",
                 "Aéroport d'arrivée 1",
                 "2023-10-26 08:00",
                 "2023-10-26 10:00",
                 "2 heures",
                 "Vol 123"
             ),
             FlightData3(
                 "Aéroport de départ 2",
                 "Aéroport d'arrivée 2",
                 "2023-10-26 11:30",
                 "2023-10-26 13:30",
                 "2 heures",
                 "Vol 456"
             )
         )

         recyclerView = findViewById(R.id.flightRecyclerView)
         recyclerView.layoutManager = LinearLayoutManager(this)
         recyclerView.adapter = FlightAdapter(flightList)
         //---------------------------------------
         // Exécutez la requête sur un thread d'arrière-plan (utilisation de Kotlin Coroutines).
         GlobalScope.launch(Dispatchers.IO) {
             fetchDataFromAPI()
         }
     }

     private fun fetchDataFromAPI() {
         val urlStr = "https://opensky-network.org/api/flights/aircraft?icao24=407183&begin=1695810322&end=1695983276"

         try {
             val url = URL(urlStr)
             val connection = url.openConnection() as HttpURLConnection

             connection.requestMethod = "GET"
             val responseCode = connection.responseCode

             if (responseCode == HttpURLConnection.HTTP_OK) {
                 val inputStream = connection.inputStream
                 val reader = BufferedReader(InputStreamReader(inputStream))
                 val response = StringBuilder()
                 var line: String?

                 while (reader.readLine().also { line = it } != null) {
                     response.append(line)
                 }

                 // Mettez à jour le TextView avec la réponse de l'API sur le thread principal.
                 runOnUiThread {
                     apiResponseTextView.text = response.toString()
                 }
             } else {
                 // Gérez les erreurs ici en cas de réponse non 200 OK.
             }
         } catch (e: Exception) {
             // Gérez les exceptions ici, par exemple, une exception d'URL mal formée.
         }
     }
 }


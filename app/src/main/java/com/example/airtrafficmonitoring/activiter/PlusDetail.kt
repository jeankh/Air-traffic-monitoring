package com.example.airtrafficmonitoring.activiter

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.airtrafficmonitoring.FlightData
import com.example.airtrafficmonitoring.R
import com.example.airtrafficmonitoring.ViewModels.HomeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PlusDetail : AppCompatActivity() {
    private lateinit var apiResponseTextView: TextView // Ajoutez cette ligne
    private lateinit var firstSeen :TextView
    private lateinit var estDepartureAirport :TextView
    private lateinit var lastSeen :TextView
    private lateinit var estArrivalAirport :TextView
    private lateinit var callsign :TextView
    private lateinit var estDepartureAirportHorizDistance: TextView
    private lateinit var estDepartureAirportVertDistance: TextView
    private lateinit var estArrivalAirportHorizDistance: TextView
    private lateinit var estArrivalAirportVertDistance: TextView
    private lateinit var departureAirportCandidatesCount: TextView
    private lateinit var arrivalAirportCandidatesCount: TextView


    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus_detail)

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            onBackPressed()
        }
        apiResponseTextView = findViewById(R.id.apiResponseTextView) // Récupérez la référence du TextView
        firstSeen = findViewById(R.id.firstSeen)
        estDepartureAirport = findViewById(R.id.estDepartureAirport)
        lastSeen = findViewById(R.id.lastSeen)
        estArrivalAirport = findViewById(R.id.estArrivalAirport)
        callsign = findViewById(R.id.callsign)
        estDepartureAirportHorizDistance = findViewById(R.id.estDepartureAirportHorizDistance)
        estDepartureAirportVertDistance = findViewById(R.id.estDepartureAirportVertDistance)
        estArrivalAirportHorizDistance = findViewById(R.id.estArrivalAirportHorizDistance)
        estArrivalAirportVertDistance = findViewById(R.id.estArrivalAirportVertDistance)
        departureAirportCandidatesCount = findViewById(R.id.departureAirportCandidatesCount)
        arrivalAirportCandidatesCount = findViewById(R.id.arrivalAirportCandidatesCount)

//---------------------------------------------------------*
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val numavion = intent.getStringExtra("icao24")
        val debutvol = intent.getStringExtra("timevol")
        val finvol = intent.getStringExtra("timevolfin")

        val urlStr = "https://opensky-network.org/api/flights/aircraft?icao24=$numavion&begin=$debutvol&end=$finvol"
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val progressText =findViewById<TextView>(R.id.progressText)
        if (networkInfo != null && networkInfo.isConnected){
            GlobalScope.launch(Dispatchers.IO) {
                fetchDataFromAPI(urlStr)
            }

        }
        else{
            runOnUiThread {
                progressText.text = "Pas de connexion Internet"
            }
        }
        //---------------------------------------
        // Exécutez la requête sur un thread d'arrière-plan (utilisation de Kotlin Coroutines).

    }

    private fun fetchDataFromAPI(urlStr:String) {

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progressText =findViewById<TextView>(R.id.progressText)
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            Log.d("attent", "thread")
            val responseCode = connection.responseCode
            Log.d("attent", "thread")
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
                Log.d("TAG", flightDataList[0].icao24.toString())
                // Mettez à jour le TextView avec la réponse de l'API sur le thread principal.
                runOnUiThread {
                    apiResponseTextView.text = flightDataList[0].icao24

                    firstSeen.text = flightDataList[0].firstSeen.toString()
                    estDepartureAirport.text = flightDataList[0].estDepartureAirport
                    lastSeen.text = flightDataList[0].lastSeen.toString()
                    estArrivalAirport.text = flightDataList[0].estArrivalAirport
                    callsign.text = flightDataList[0].callsign
                    estDepartureAirportHorizDistance.text = flightDataList[0].estDepartureAirportHorizDistance.toString()
                    estDepartureAirportVertDistance.text = flightDataList[0].estDepartureAirportVertDistance.toString()
                    estArrivalAirportHorizDistance.text = flightDataList[0].estArrivalAirportHorizDistance.toString()
                    estArrivalAirportVertDistance.text = flightDataList[0].estArrivalAirportVertDistance.toString()
                    departureAirportCandidatesCount.text = flightDataList[0].departureAirportCandidatesCount.toString()
                    arrivalAirportCandidatesCount.text = flightDataList[0].arrivalAirportCandidatesCount.toString()

                }
            }
                else{
                runOnUiThread {
                    progressText.text="pas de signal"
                }
                }


    }
}



/**[
{
    "icao24": "407183",
   "firstSeen": 1695810323,
      "estDepartureAirport": "LEMG",
  "lastSeen": 1695819725,
  "estArrivalAirport": "EGNT",
  "callsign": "EXS39A  ",
  "estDepartureAirportHorizDistance": 5996,
  "estDepartureAirportVertDistance": 425,
  "estArrivalAirportHorizDistance": 1049,
  "estArrivalAirportVertDistance": 6708,
  "departureAirportCandidatesCount": 19,
  "arrivalAirportCandidatesCount": 0
  }
  ]
 **/

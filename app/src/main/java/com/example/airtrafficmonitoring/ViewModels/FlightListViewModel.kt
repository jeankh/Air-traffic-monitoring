package com.example.airtrafficmonitoring.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airtrafficmonitoring.FlightModel
import com.example.airtrafficmonitoring.RequestManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
class FlightListViewModel : ViewModel() {

    private val flightListLiveData = MutableLiveData<List<FlightModel>>(ArrayList())
    private val clickedFlightLiveData = MutableLiveData<FlightModel>()

    fun getFlightListLiveData(): LiveData<List<FlightModel>> {
        return flightListLiveData
    }

    private fun setFlightListLiveData(flights: List<FlightModel>) {
        flightListLiveData.value = flights
    }

    fun getClickedFlightLiveData(): LiveData<FlightModel> {
        return clickedFlightLiveData
    }

    fun setClickedFlightLiveData(flight: FlightModel) {
        clickedFlightLiveData.value = flight
    }

    fun doRequest(begin: Long, end: Long, isArrival: Boolean, icao: String) {

        viewModelScope.launch {

            val url = if (isArrival) "https://opensky-network.org/api/flights/arrival" else "https://opensky-network.org/api/flights/departure"
            val params = HashMap<String, String>()
            params.put("begin", begin.toString())
            params.put("end", end.toString())
            params.put("airport", icao)

            val result = withContext(Dispatchers.IO) {
                RequestManager.getSuspended(url, params)
            }
            if (result != null) {
                Log.i("REQUEST", result)

                val flightList = ArrayList<FlightModel>()
                val parser = JsonParser()
                val jsonElement = parser.parse(result)

                for (flightObject in jsonElement.asJsonArray) {
                    flightList.add(Gson().fromJson(flightObject.asJsonObject, FlightModel::class.java))
                }

                setFlightListLiveData(flightList)
                // Equivalent à
                //flightListLiveData.value =  flightList

            } else {
                Log.e("REQUEST", "ERROR NO RESULT")
            }

        }

    }
}
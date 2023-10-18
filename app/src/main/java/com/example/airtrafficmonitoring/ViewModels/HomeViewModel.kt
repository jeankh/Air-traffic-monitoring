package com.example.airtrafficmonitoring.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airtrafficmonitoring.Airport

class HomeViewModel : ViewModel() {
    private val airportListData = MutableLiveData<List<Airport>>()

    fun setAirportList(airportList: List<Airport>) {
        airportListData.value = airportList
    }

    fun getAirportList(): LiveData<List<Airport>> {
        return airportListData
    }
}


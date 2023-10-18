package com.example.airtrafficmonitoring.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airtrafficmonitoring.Airport


class DetailFlyViewModel : ViewModel() {
    private val selectedAirport = MutableLiveData<Airport>()

    fun setSelectedAirport(airport: Airport) {
        selectedAirport.value = airport
    }

    fun getSelectedAirport(): LiveData<Airport> {
        return selectedAirport
    }
}

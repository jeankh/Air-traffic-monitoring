package com.example.airtrafficmonitoring.ViewModels

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airtrafficmonitoring.Airport
import com.example.airtrafficmonitoring.RequestManager
import com.example.airtrafficmonitoring.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val beginDateLiveData = MutableLiveData<Calendar>(Calendar.getInstance())
    private val endDateLiveData = MutableLiveData<Calendar>(Calendar.getInstance())

    private val airportListLiveData = MutableLiveData<List<Airport>>()
    private val airportListNamesLiveData = MutableLiveData<List<String>>()

    init{
        val airportList = Utils.generateAirportList()
        airportListLiveData.value = airportList
        val airportNamesList = ArrayList<String>()
        //Populate the list of airport names
        for (airport in airportList) {
            airportNamesList.add(airport.getFormattedName())
        }
        airportListNamesLiveData.value = airportNamesList
    }

    enum class DateType {
        BEGIN, END
    }

    fun getBeginDateLiveData(): LiveData<Calendar>{
        return beginDateLiveData
    }

    fun getEndDateLiveData(): LiveData<Calendar>{
        return endDateLiveData
    }

    fun updateCalendarLiveData(dateType: DateType, calendar: Calendar){
        if(dateType == DateType.BEGIN) beginDateLiveData.value = calendar else endDateLiveData.value = calendar
    }

    fun getAirportNamesListLiveData():LiveData<List<String>>{
        return airportListNamesLiveData
    }

    fun getAirportListLiveData():LiveData<List<Airport>>{
        return airportListLiveData
    }
}
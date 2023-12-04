package com.example.airtrafficmonitoring.activiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airtrafficmonitoring.FlightMapFragment
import com.example.airtrafficmonitoring.R
import com.example.airtrafficmonitoring.ViewModels.FlightListViewModel

class FlightListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        val isTablet = findViewById<FragmentContainerView>(R.id.fragment_map_container) != null


        val begin = intent.getLongExtra("BEGIN", 0)
        val end = intent.getLongExtra("END", 0)
        val isArrival = intent.getBooleanExtra("IS_ARRIVAL", false)
        val icao = intent.getStringExtra("ICAO")

        val viewModel = ViewModelProvider(this).get(FlightListViewModel::class.java)

        Log.i("MAIN ACTIVITY", "begin = $begin \n end = $end \n icao = $icao \n is arrival = $isArrival")

        // DO NOT DO REQUEST IN ACTIVITY LIKE THE COMMENT BELOW
        //RequestManager.get("https://google.fr", HashMap())

        viewModel.doRequest(begin, end, isArrival, icao!!)

        viewModel.getClickedFlightLiveData().observe(this, Observer {
            // Afficher le bon vol

            if (!isTablet) {
                //remplacer le fragment
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_list_container, FlightMapFragment.newInstance("", ""))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })

    }


}
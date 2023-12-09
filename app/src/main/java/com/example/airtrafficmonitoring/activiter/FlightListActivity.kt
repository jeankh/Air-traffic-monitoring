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
import com.example.airtrafficmonitoring.FlightListAdapter
import com.example.airtrafficmonitoring.FlightMapFragment
import com.example.airtrafficmonitoring.FlightModel
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

        viewModel.doRequest(begin, end, isArrival, icao!!, applicationContext)

        viewModel.getFlightListLiveData().observe(this, Observer { flights ->
            updateFlightList(flights)
        })

        if (!isTablet) {
            viewModel.getClickedFlightLiveData().observe(this, Observer {
                // Replace the fragment
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_list_container, FlightMapFragment.newInstance("", ""))
                transaction.addToBackStack(null)
                transaction.commit()
            })
        }
    }

    private fun updateFlightList(flights: List<FlightModel>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val flightListAdapter = FlightListAdapter(flights, object : FlightListAdapter.OnCellClickListener {
            override fun onCellClicked(flightModel: FlightModel) {
                // Handle click events on flight cells
                // For example, if needed: viewModel.setClickedFlightLiveData(flightModel)
            }
        })
        recyclerView.adapter = flightListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
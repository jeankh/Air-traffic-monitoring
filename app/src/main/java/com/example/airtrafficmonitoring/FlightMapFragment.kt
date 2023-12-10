package com.example.airtrafficmonitoring
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.airtrafficmonitoring.ViewModels.FlightListViewModel
import com.example.airtrafficmonitoring.activiter.InfoMap

class FlightMapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flight_map, container, false)

        // Get the FrameLayout to contain InfoMap
        val infoMapContainer = view.findViewById<FrameLayout>(R.id.infoMapContainer)

        // Check if the InfoMap activity is not already added
        if (infoMapContainer.childCount == 0) {
            // Inflate the InfoMap activity into the FrameLayout
            val infoMapIntent = Intent(requireContext(), InfoMap::class.java)
            startActivity(infoMapIntent)
        }

        return view
    }
}
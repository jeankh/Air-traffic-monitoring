package com.example.airtrafficmonitoring

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airtrafficmonitoring.ViewModels.FlightListViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FlightListFragment : Fragment(), FlightListAdapter.OnCellClickListener {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel : FlightListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FlightListViewModel::class.java]

        viewModel.getFlightListLiveData().observe(viewLifecycleOwner, Observer {
            //findViewById<TextView>(R.id.textView).text = it.toString()
            Log.d("FlightListFragment", "Flight list size: ${it.size}")
            //Récupérer le recyclerView
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
            // Attacher un Adapter
            recyclerView.adapter = FlightListAdapter(it, this)
            // Attacher un LayoutManager
            recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FlightListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCellClicked(flightModel: FlightModel) {
        viewModel.setClickedFlightLiveData(flightModel)
    }
}
package com.example.airtrafficmonitoring.activiter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.airtrafficmonitoring.FlightData
import com.example.airtrafficmonitoring.R
import com.example.airtrafficmonitoring.ViewModels.HomeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class InfoMap : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var cardView: CardView
    private lateinit var showDetailsButton: Button
    private lateinit var detailplus: Button
    private lateinit var detail3jours: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_map)

        // Configure OSMdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdata", 0))
        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        val overlayItems = ItemizedIconOverlay<OverlayItem>(this, ArrayList(), null)
        // Set the initial map center and zoom level
        mapView.controller.setZoom(5.0)
        mapView.controller.setCenter(GeoPoint(48.8566, 2.3522)) // London coordinates

        // TODO recupee identifiant de l'avaion sont depart et arriver
        /**var intent: Intent? = getIntent()
        var numavion = intent!!.getStringExtra("icao24")
        var aerodep= intent!!.getStringExtra("estDepartureAirport")
        var aeroariv = intent!!.getStringExtra("estArrivalAirport")
        var timevol = intent!!.getStringExtra("timeAirport")

        val parisAirport = OverlayItem("Paris Airport", "Charles de Gaulle Airport", GeoPoint(49.0097, 2.5479))
        val lyonAirport = OverlayItem("Lyon Airport", "Lyon-Saint Exupéry Airport", GeoPoint(45.7215, 5.0824))
         **/

        // Add two airports as overlay items
        val parisAirport = OverlayItem("Paris Airport", "Charles de Gaulle Airport", GeoPoint(38.9435, 20.7352))
        val lyonAirport = OverlayItem("Lyon Airport", "Lyon-Saint Exupéry Airport", GeoPoint( 54.4852, -1.8094))

        overlayItems.addItem(parisAirport)
        overlayItems.addItem(lyonAirport)


        // Add the overlay items and polyline to the map
        mapView.overlays.add(overlayItems)

        cardView = findViewById(R.id.cardView)
        showDetailsButton = findViewById(R.id.showDetailsButton)
        // Accédez au TextView contenant "Détails du vol"
        val textViewDetailDuVol: TextView = cardView.findViewById(R.id.aerodep)
        textViewDetailDuVol.text=parisAirport.title
        showDetailsButton.setOnClickListener {
            // Show the CardView when the button is clicked
            if (cardView.visibility == View.VISIBLE)
            {  cardView.visibility = View.GONE
            } else{            cardView.visibility = View.VISIBLE
            }

        }
        // aller a la page 3jours
        detail3jours = findViewById(R.id.detail3jours)
        detail3jours.setOnClickListener {
            val intent = Intent(this, Detail3jour ::class.java)
            //intent.putExtra("icao24", numavion)
            //intent.putExtra("timevol", timevol)
            //intent.putExtra("timevolfin", timevolfin)
            startActivity(intent)
        }
        //aller a la page datail+
        detailplus = findViewById(R.id.detailplus)
        detailplus.setOnClickListener {
            val intent = Intent(this, PlusDetail ::class.java)
            intent.putExtra("icao24", "407183")
            intent.putExtra("timevol", "1695810322")
            intent.putExtra("timevolfin", "1695819725")
            startActivity(intent)
        }
        GlobalScope.launch(Dispatchers.IO) {
            val apiUrl = "https://opensky-network.org/api/tracks/all?icao24=407183&time=0"
            val url = URL(apiUrl)
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
                val jsonObject = JSONObject(response.toString())
                val pathArray = jsonObject.getJSONArray("path")

                // Créez une seule instance de Polyline en dehors de la boucle
                val polyline = Polyline()

                for (i in 0 until pathArray.length()) {
                    val point = pathArray.getJSONArray(i)
                    val latitude = point.getDouble(1)
                    val longitude = point.getDouble(2)

                    val geoPoint = GeoPoint(latitude, longitude)

                    polyline.addPoint(geoPoint)
                }

                // Ajoutez la Polyline à la carte en dehors de la boucle
                mapView.overlays.add(polyline)

            }
            mapView.invalidate()
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}








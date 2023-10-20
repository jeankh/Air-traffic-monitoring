package com.example.airtrafficmonitoring.activiter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.airtrafficmonitoring.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import androidx.cardview.widget.CardView
import android.view.View
import android.widget.Button
import android.widget.TextView

class InfoMap : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var cardView: CardView
    private lateinit var showDetailsButton: Button
    private lateinit var detailplus: Button
    private lateinit var detail3jours: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_map)

        detail3jours = findViewById(R.id.detail3jours)
        detail3jours.setOnClickListener {
            val intent = Intent(this, Detail3jour ::class.java)
            startActivity(intent)
        }

        detailplus = findViewById(R.id.detailplus)
        detailplus.setOnClickListener {
            val intent = Intent(this, PlusDetail ::class.java)
            startActivity(intent)
        }
        // Configure OSMdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdata", 0))
        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        // Set the initial map center and zoom level
        mapView.controller.setZoom(5.0)
        mapView.controller.setCenter(GeoPoint(48.8566, 2.3522)) // London coordinates
        val overlayItems = ItemizedIconOverlay<OverlayItem>(this, ArrayList(), null)
// Add two airports as overlay items
        val parisAirport = OverlayItem("Paris Airport", "Charles de Gaulle Airport", GeoPoint(49.0097, 2.5479))
        val lyonAirport = OverlayItem("Lyon Airport", "Lyon-Saint Exupéry Airport", GeoPoint(45.7215, 5.0824))

        overlayItems.addItem(parisAirport)
        overlayItems.addItem(lyonAirport)

        // Create a Polyline to draw the airplane route
        val polyline = Polyline()
        polyline.color = android.graphics.Color.BLUE // Set the color of the line
        polyline.width = 4.0f // Set the width of the line
        polyline.addPoint(parisAirport.point as GeoPoint ) // Add Paris Airport as the starting point
        polyline.addPoint(lyonAirport.point as GeoPoint)  // Add Lyon Airport as the ending point

        // Add the overlay items and polyline to the map
        mapView.overlays.add(overlayItems)
        mapView.overlays.add(polyline)
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
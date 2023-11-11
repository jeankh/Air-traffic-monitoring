package com.example.airtrafficmonitoring.activiter

import InfoMapViewModel
import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.airtrafficmonitoring.FlightData
import com.example.airtrafficmonitoring.R
import com.example.airtrafficmonitoring.ViewModels.HomeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.Marker
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
    private lateinit var viewModel: InfoMapViewModel
    lateinit var numavion33: String

    @SuppressLint("WrongViewCast", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_map)
        viewModel = ViewModelProvider(this).get(InfoMapViewModel::class.java)

        Log.d("thoma", viewModel.toString())

        // Configure OSMdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdata", 0))
        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        val overlayItems = ItemizedIconOverlay<OverlayItem>(this, ArrayList(), null)
        // Set the initial map center and zoom level
        mapView.controller.setZoom(5.0)
        mapView.controller.setCenter(GeoPoint(48.8566, 2.3522)) // London coordinates
        /**
         var intent: Intent? = getIntent()
        var numavion = intent!!.getStringExtra("icao24")
        var aerodep= intent!!.getStringExtra("estDepartureAirport")
        var aeroariv = intent!!.getStringExtra("estArrivalAirport")
        var timevol = intent!!.getStringExtra("timeAirport")

        val parisAirport = OverlayItem("Paris Airport", "Charles de Gaulle Airport", GeoPoint(49.0097, 2.5479))
        val lyonAirport = OverlayItem("Lyon Airport", "Lyon-Saint Exupéry Airport", GeoPoint(45.7215, 5.0824))
         **/
        var numavion33 = "4ca76a"
        var aerodep33= "EGGW"
        var aeroariv33 = "LTBD"
        var timevol33 = "0"



        cardView = findViewById(R.id.cardView)
        showDetailsButton = findViewById(R.id.showDetailsButton)
        // Accédez au TextView contenant "Détails du vol"

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

            //calcul trois jours
            intent.putExtra("icao24", numavion33)
            intent.putExtra("timevol", "1695878586")
            intent.putExtra("timevolfin", "1696137786")
            startActivity(intent)
        }
        //aller a la page datail+
        detailplus = findViewById(R.id.detailplus)
        detailplus.setOnClickListener {
            val intent = Intent(this, PlusDetail ::class.java)
            intent.putExtra("icao24", numavion33)
            intent.putExtra("timevol", "1695878586")
            intent.putExtra("timevolfin", "1696137786")
            startActivity(intent)
        }
        val progressText =findViewById<TextView>(R.id.progressText)
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo


        if (networkInfo != null && networkInfo.isConnected ) {
            Log.d("internet", "internat")
            val sauvegarde = viewModel.parisAirportLiveData.value
            if (sauvegarde == null) {
                Log.d("celacela", "internat")
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    var apiUrl =
                        "https://opensky-network.org/api/tracks/all?icao24=$numavion33&time=$timevol33"
                    val progressBar = findViewById<ProgressBar>(R.id.progressBar)

                    val url = URL(apiUrl)

                    val connection = url.openConnection() as HttpURLConnection
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.VISIBLE
                    }


                    connection.requestMethod = "GET"
                    Log.d("reponse", "responsecode")
                    val responseCode = connection.responseCode

                    Log.d("reponse", "avant le if ")
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Log.d("reponse", "apres le if ")
                        val inputStream = connection.inputStream
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val response = StringBuilder()
                        var line: String?

                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        Log.d("toto", response.toString())

                        val jsonObject = JSONObject(response.toString())
                        val pathArray = jsonObject.getJSONArray("path")

                        val dernierElement = pathArray[pathArray.length() - 1]
                        if (dernierElement is JSONArray) {
                            // Assurez-vous que c'est bien un tableau JSON
                            val latitude = dernierElement.opt(1)
                            val longitude = dernierElement.opt(2)
                            if (latitude != null) {
                                val latitude = latitude.toString().toDouble()
                                val longitude = longitude.toString().toDouble()
                                val aeroportarriver= Marker(mapView)
                                aeroportarriver.position = GeoPoint(latitude, longitude)
                                aeroportarriver.title = "Paris Airport"
                                aeroportarriver.snippet = "Charles de Gaulle Airport"
                                val iconWidth = 100 // Largeur souhaitée en pixels
                                val iconHeight = 100 // Hauteur souhaitée en pixels
                                val iconDrawable =
                                    resources.getDrawable(R.drawable.img)
                                if (iconDrawable != null) {
                                    val anchorX = 0.2f // Point d'ancrage horizontal au centre (0.5)
                                    val anchorY = 0.2f // Point d'ancrage vertical au bas (1.0)
                                    aeroportarriver.setAnchor(anchorX, anchorY)
                                    val scaledIcon = Bitmap.createScaledBitmap(
                                        (iconDrawable as BitmapDrawable).bitmap,
                                        iconWidth,
                                        iconHeight,
                                        false
                                    )
                                    // Créez un Drawable à partir de l'image redimensionnée
                                    val scaledIconDrawable = BitmapDrawable(resources, scaledIcon)
                                    // Attribuez l'icône redimensionnée au marqueur
                                    aeroportarriver.icon = scaledIconDrawable
                                    viewModel.updateaeroportarriver(aeroportarriver)
                                    mapView.overlays.add(aeroportarriver)
                                }
                                // Add the overlay items and polyline to the map
                                mapView.overlays.add(overlayItems)
                            }

                        }

                        val premierElement = pathArray.optJSONArray(0)
                        if (premierElement != null) {
                            val latitudePremier = premierElement.opt(1)
                            val longitudePremier = premierElement.opt(2)

                            if (latitudePremier != null) {
                                val latitudePremierValue = latitudePremier.toString().toDouble()
                                val longitudePremierValue = longitudePremier.toString().toDouble()

                                val autreMarqueur = Marker(mapView)
                                autreMarqueur.position =
                                    GeoPoint(latitudePremierValue, longitudePremierValue)
                                autreMarqueur.title = "aeroport de depart"
                                autreMarqueur.snippet = "Description"
                                viewModel.updateaeroportdep(autreMarqueur)
                                mapView.overlays.add(autreMarqueur)

                            }
                        }
                        Log.d("last", response.toString())
                        // Créez une seule instance de Polyline en dehors de la boucle
                        val polyline = Polyline()
                        val geoPointsList = mutableListOf<GeoPoint>()
                        for (i in 0 until pathArray.length()) {
                            val point = pathArray.getJSONArray(i)
                            val latitude = point.getDouble(1)
                            val longitude = point.getDouble(2)

                            val geoPoint = GeoPoint(latitude, longitude)
                            geoPointsList.add(geoPoint)
                            polyline.addPoint(geoPoint)
                        }

                        Log.d("toto", "Ceci est un message de débogage.")
                        // Ajoutez la Polyline à la carte en dehors de la boucle
                        viewModel.updatePolyline(geoPointsList)
                        mapView.overlays.add(polyline)

                        withContext(Dispatchers.Main) {
                            progressBar.visibility = View.GONE
                            progressText.visibility = View.GONE

                        }


                    } else {
                        withContext(Dispatchers.Main) {
                            progressText.text = "pas de signal"
                        }
                    }
                    mapView.invalidate()
                }
            }
            else{
                viewModel.parisAirportLiveData.observe(this, Observer { parisAirport ->
                    val testMarker = Marker(mapView)
                    testMarker.position = GeoPoint(parisAirport.position.latitude, parisAirport.position.longitude) // Coordonnées de Paris
                    testMarker.title = parisAirport.title
                    mapView.overlays.add(testMarker)
                    mapView.invalidate()

                })
                viewModel.depmarkerLiveData.observe(this, Observer { depmarkerLiveData ->
                    val anotherMarker = Marker(mapView)
                    anotherMarker.position = GeoPoint( depmarkerLiveData.position.latitude,  depmarkerLiveData.position.longitude)
                    anotherMarker.title =  depmarkerLiveData.title
                    mapView.overlays.add(anotherMarker)
                    mapView.invalidate()
                })
                viewModel.polylineLiveData.observe(this, Observer { geoPoints ->
                    if (geoPoints.isNotEmpty()) {
                        val newPolyline = Polyline()
                        geoPoints.forEach { geoPoint ->
                            newPolyline.addPoint(geoPoint)
                        }
                        mapView.overlays.add(newPolyline)
                        mapView.invalidate()
                    }
                })





            }
    } else {
        // Pas de connexion Internet, affichez un message d'erreur ou effectuez une action appropriée
            progressText.text = "Pas de connexion Internet"
    }
        mapView.invalidate()
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








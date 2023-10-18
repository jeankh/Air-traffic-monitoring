package com.example.airtrafficmonitoring.activiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.airtrafficmonitoring.R

class liste_result : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_result)

        // Récupérez les données transmises par l'Intent
        val aeroportDepart = intent.getStringExtra("AeroportDepart")
        val aeroportArrivee = intent.getStringExtra("AeroportArrivee")
        val dateDepart = intent.getStringExtra("DateDepart")
        val dateArrivee = intent.getStringExtra("DateArrivee")

        // Vous pouvez utiliser ces données comme bon vous semble dans cette activité

        Log.d("MyApp2", "Aéroport de départ : $aeroportDepart")
        Log.d("MyApp2", "Aéroport d'arrivée : $aeroportArrivee")
        Log.d("MyApp2", "Date de départ : $dateDepart")
        Log.d("MyApp2", "Date d'arrivée : $dateArrivee")


    }
}


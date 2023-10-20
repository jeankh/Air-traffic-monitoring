package com.example.airtrafficmonitoring.activiter


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.airtrafficmonitoring.R
import com.example.airtrafficmonitoring.Utils


class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val airportList = Utils.generateAirportList()
        val spinner = findViewById<Spinner>(R.id.airport_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item , airportList)
        spinner.adapter = adapter

        val button = findViewById<Button>(R.id.buttons)

        button.setOnClickListener(View.OnClickListener { // Créez une intention (Intent) pour ouvrir la nouvelle activité
        //   val aeroportDepart = editTextDepart.text.toString()
            //  val aeroportArrivee = editTextArrivee.text.toString()
            // val dateDepart = editTextDateDepart.text.toString()
            // val dateArrivee = editTextDateArrivee.text.toString()

            // Créez un Intent pour passer les données à la nouvelle activité
            // val intent = Intent(this, ListeResult::class.java)

            // Ajoutez les données en extra dans l'Intent
            // intent.putExtra("AeroportDepart", aeroportDepart)
            // intent.putExtra("AeroportArrivee", aeroportArrivee)
            // intent.putExtra("DateDepart", dateDepart)
            // intent.putExtra("DateArrivee", dateArrivee)
            // Démarrez la nouvelle activité
            startActivity(intent)
        })
//        findViewById<TextView>(R.id.from_date_view).setOnClickListener {
//            showDatePickerDialog()
//        }
//
//
//
//     fun showDatePickerDialog() {
//        val dateSetListener = OnDateSetListener {view, year, monthOfYear, dayOfMonth ->
//            val calendar = Calendar.getInstance()
//            calendar.set(Calendar.YEAR, year)
//            calendar.set(Calendar.MONTH, monthOfYear)
//            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            findViewById<TextView>(R.id.from_date_view).text= Utils.dateToString(calendar.time)
//        }
//
//        val calendar = Calendar.getInstance()
//        val datePickerDialog = DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
//         datePickerDialog.show()
//
//        }
//
//    }



    }
}
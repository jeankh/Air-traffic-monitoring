import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airtrafficmonitoring.FlightData3

class Detail3jourViewModel : ViewModel() {
    // Variable LiveData pour sauvegarder la liste de vols
    private val flightListLiveData = MutableLiveData<List<FlightData3>>()

    // Méthode pour mettre à jour la liste de vols
    fun setFlightList(flightList: List<FlightData3>) {
        flightListLiveData.value = flightList
    }

    // Méthode pour obtenir la liste de vols
    fun getFlightList(): LiveData<List<FlightData3>> {
        return flightListLiveData
    }
}

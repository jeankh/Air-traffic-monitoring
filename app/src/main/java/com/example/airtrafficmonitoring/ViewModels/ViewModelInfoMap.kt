import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class InfoMapViewModel : ViewModel() {

    private val _parisAirportLiveData = MutableLiveData<Marker>()
    val parisAirportLiveData: LiveData<Marker>
        get() = _parisAirportLiveData

    private val _polylineLiveData = MutableLiveData<List<GeoPoint>>() // Utilisez List<GeoPoint> au lieu de Polyline
    val polylineLiveData: LiveData<List<GeoPoint>>
        get() = _polylineLiveData

    private val _depmarkerLiveData = MutableLiveData<Marker>()
    val depmarkerLiveData: LiveData<Marker>
        get() = _depmarkerLiveData

    init {
        Log.d("ViewModel", "ViewModel created: $this")
    }

    // Méthode pour mettre à jour le marqueur Paris Airport
    fun updateaeroportarriver(marker: Marker) {
        _parisAirportLiveData.postValue(marker)
    }

    // Méthode pour mettre à jour le Polyline
    fun updatePolyline(geoPoints: List<GeoPoint>) { // Utilisez List<GeoPoint> au lieu de Polyline
        Log.d("totota", "rrr $geoPoints")
        _polylineLiveData.postValue(geoPoints)
    }

    // Méthode pour mettre à jour l'autre Marker
    fun updateaeroportdep(marker: Marker) {
        _depmarkerLiveData.postValue(marker)
    }
}

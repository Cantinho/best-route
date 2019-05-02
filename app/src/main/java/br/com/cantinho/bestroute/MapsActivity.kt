package br.com.cantinho.bestroute

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.cantinho.bestroute.models.Rider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        loadRiders()
    }


    var ACCESS_LOCATION = 123
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION)
                return
            }
        }
        getUserLocation()
    }

    fun getUserLocation() {
        Toast.makeText(this, "User location access on", Toast.LENGTH_SHORT).show()
        var myLocationListener = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocationListener)
        var myThread = MyThread()
        myThread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, "We cannot access your location", Toast.LENGTH_SHORT).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    var myLocation:Location? = null

    inner class MyLocationListener : LocationListener {

        constructor() {
            myLocation = Location("Start")
            myLocation!!.longitude = 0.0
            myLocation!!.latitude = 0.0
        }

        override fun onLocationChanged(location: Location?) {
            myLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    var oldLocation:Location? = null
    inner class MyThread : Thread {
        constructor() : super() {
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0
        }

        override fun run() {
            while (true) {
                try {
                    if(oldLocation!!.distanceTo(myLocation) == 0f) {
                        continue
                    }
                    oldLocation = myLocation
                    myLocation?.let {
                        runOnUiThread{

                            mMap!!.clear()

                            // show me
                            // Add a marker in Sydney and move the camera
                            val sydney = LatLng(myLocation!!.latitude, myLocation!!.longitude)

                            //        val circleDrawable = resources.getDrawable(R.drawable.caynan)
                            //        var bitmapDescriptor = getMarkerIconFromDrawable(this, circleDrawable)

                            mMap.addMarker(
                                MarkerOptions().position(sydney)
                                    .title("Marker in Sydney")
                                    .snippet("Here is my position.")
                                //            .icon(bitmapDescriptor)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.samir_30))
                            )
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))

                            // show riders
                            listOfRiders.forEach {
                                if(it.isCatch == false) {
                                    val riderLocation = LatLng(it.latitude!!, it.longitude!!)
                                    mMap.addMarker(
                                        MarkerOptions().position(riderLocation)
                                            .title(it.name)
                                            .snippet(it.description)
                                            .icon(BitmapDescriptorFactory.fromResource(it.image!!)))

                                    val riderLoc = Location(it.name)
                                    riderLoc.latitude = riderLocation.latitude
                                    riderLoc.longitude = riderLocation.longitude
                                    if(myLocation!!.distanceTo(riderLoc) < 2) {
                                        it.isCatch = true
                                        Toast.makeText(applicationContext,
                                            "You catch new rider: " +it.name,
                                            Toast.LENGTH_LONG).show()
                                    }
                                }
                            }


                        }
                    }

                    Thread.sleep(1000)

                } catch (exc: Exception) {

                }
            }
        }
    }



    var listOfRiders =  ArrayList<Rider>()
    fun loadRiders() {
        listOfRiders.add(Rider("Samir Clone", "Virtus", R.drawable.samirclone_30, latitude = -7.216386399999999, longitude =  -35.914598494742776))
        listOfRiders.add(Rider("Caynan", "Sandros Bar", R.drawable.caynan_30, latitude = -7.244865, longitude =  -35.876935))
        listOfRiders.add(Rider("Bruno", "Residencial Hermione", R.drawable.bruno_30, latitude = -7.241235, longitude =  -35.893387))
        listOfRiders.add(Rider("Guilherme", "TMSC", R.drawable.guilherme_30, latitude = -7.221092, longitude =  -35.887743))
        listOfRiders.add(Rider("Sampaio", "Bar do Cuzcuz", R.drawable.sampaio_30, latitude = -7.222778, longitude =  -35.877847))
        listOfRiders.add(Rider("Batista", "Taverna", R.drawable.batista_30, latitude = -7.236935, longitude =  -35.890429))
    }
}

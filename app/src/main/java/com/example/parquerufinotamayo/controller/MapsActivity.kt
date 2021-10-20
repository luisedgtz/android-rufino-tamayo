
package com.example.parquerufinotamayo.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.parquerufinotamayo.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var marker: MarkerOptions
    private lateinit var btnSetLocation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        marker = MarkerOptions()
        btnSetLocation = findViewById(R.id.btnSetLocation)

        btnSetLocation.setOnClickListener(onSetLocationListener())

    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val lat: Double = intent.getStringExtra("lat")!!.toDouble()
        val lng: Double = intent.getStringExtra("lng")!!.toDouble()

        val startPoint = LatLng(lat, lng)
        marker.position(startPoint)
        mMap.addMarker(marker)
        mMap.setMinZoomPreference(18f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint))
        mMap.setOnMapClickListener {
            mMap.clear()
            marker.position(it)
            mMap.addMarker(marker)
        }
    }

    private fun onSetLocationListener(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent()
            intent.putExtra("Lat", marker.position.latitude.toString())
            intent.putExtra("Lng", marker.position.longitude.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
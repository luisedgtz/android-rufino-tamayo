package com.example.parquerufinotamayo.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.R
import com.example.parquerufinotamayo.model.repository.RemoteRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var tvTitle : TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDescription: TextView
    private lateinit var ivPhoto: ImageView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapDetails) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tvTitle = findViewById(R.id.tvReportDetailsTitle)
        tvCategory = findViewById(R.id.tvReportDetailsCategory)
        tvDate = findViewById(R.id.tvReportDetailsDate)
        tvDescription = findViewById(R.id.tvReportDetailsDescription)
        ivPhoto = findViewById(R.id.ivReportDetailsPhoto)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        fillData()
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val lat: Double = intent.getStringExtra("lat")!!.toDouble()
        val lng: Double = intent.getStringExtra("long")!!.toDouble()

        val startPoint = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(startPoint))
        mMap.setMinZoomPreference(18f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint))
    }

    private fun fillData() {
        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val date = intent.getSerializableExtra("creationDate") as Date
        val description = intent.getStringExtra("description")
        val images = intent.getStringArrayExtra("images")

        tvTitle.text = title
        tvCategory.text = category
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val stringDate = sdf.format(date)
        tvDate.text = stringDate
        tvDescription.text = description
        if (images?.isNotEmpty() == true){
            askForImage(images!![0])
        }
    }

    private fun askForImage(photoPath: String) {
        val picasso = RemoteRepository.getPicassoInstance(this, LoginUtils.getToken(this))
        val urlForImage = "${LoginUtils.BASE_URL}reports/images/$photoPath"
        picasso.load(urlForImage).into(ivPhoto);
    }
}
package com.example.parquerufinotamayo.controller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.R
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.parquerufinotamayo.model.Model
import com.example.parquerufinotamayo.model.entities.Category
import com.example.parquerufinotamayo.model.entities.Report
import com.example.parquerufinotamayo.model.repository.responseinterface.IGetAllCategories
import com.example.parquerufinotamayo.model.repository.responseinterface.INewReport
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class NewReportFragment : Fragment() {
    companion object{
        private const val CAMERA_PERMISSION_CODE = 100
        private const val FINE_LOCATION_PERMISSION_CODE = 101
    }

    private lateinit var btnTakePhoto : Button
    private lateinit var btnEndRp : Button
    private lateinit var btnFromGallery : Button
    private lateinit var editTxtRpTitle : EditText
    private lateinit var editTxtDescription : EditText
    private lateinit var reportImage: ImageView
    private lateinit var txtCategories : AutoCompleteTextView
    private lateinit var btnUbi : Button

    private lateinit var model : Model
    private lateinit var imageByteArray: ByteArray

    private lateinit var cancellationTokenSource: CancellationTokenSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        model = Model(LoginUtils.getToken(requireContext()))
        imageByteArray = ByteArray(0)
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto)
        btnEndRp = view.findViewById(R.id.btnEndRp)
        //btnFromGallery = view.findViewById(R.id.btnUbi)
        btnUbi = view.findViewById(R.id.btnUbi)
        reportImage = view.findViewById(R.id.reportImage)
        editTxtDescription = view.findViewById(R.id.editTxtDescription)
        editTxtRpTitle = view.findViewById(R.id.editTxtRpTitle)
        txtCategories = view.findViewById(R.id.txtReportSolve)
        val lista = getSpinnerList()

        val adapter = context?.let { ArrayAdapter(it, R.layout.list_item, lista) };
        txtCategories.setAdapter(adapter)

        btnTakePhoto.setOnClickListener(clickListenerForTakePhoto())
        btnEndRp.setOnClickListener(clickListenerForNewReport())
        //btnFromGallery.setOnClickListener()
        btnUbi.setOnClickListener(clickForUbi())
    }

    private fun clickForUbi(): View.OnClickListener? {
        return View.OnClickListener {
            val permissionStatus = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(requireContext(), "No hay acceso a la ubicación", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_PERMISSION_CODE
                )
            } else {
                Log.i("tag", "we have the permission, thanks")
                getUbi()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUbi() {
        cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnCanceledListener{
            Log.i(
                "currentLocationListener",
                "The location update request has been cancejled"
            )
        }.addOnSuccessListener { location ->
            Log.i(
                "currentLocationListener",
                "Location Obtained! ${location.toString()}"
            )
            val sb = StringBuffer()
            sb.append(editTxtDescription.text).append("\n").append("Latitud: ").append((location.latitude)).append(", Longitud: ").append(location.longitude)
            editTxtDescription.setText(sb.toString())
            Toast.makeText(requireContext(), "Ubicación agregada", Toast.LENGTH_SHORT).show()
            btnUbi.isEnabled = false
        }
    }

    private fun getSpinnerList(): ArrayList<String> {
        val categoryList = ArrayList<String>()
        model.getAllCategories(object : IGetAllCategories{
            override fun onSuccess(categories: List<Category>?) {
                if(categories != null){
                    for(category in categories){
                        categoryList.add(category.name.toString())
                    }
                }
            }

            override fun onNoSuccess(code: Int, message: String) {
                Toast.makeText(
                    requireContext(),
                    "Problem detected $code $message",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("getCatgs", "$code: $message")
            }

            override fun onFailure(t: Throwable) {
                Toast.makeText(
                    context,
                    "Network or server error occurred",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("addUser", t.message.toString())
            }
        })
        return categoryList
    }

    private fun clickListenerForNewReport(): View.OnClickListener {
        return View.OnClickListener {
            val txtReportTitle = requireView().findViewById<EditText>(R.id.editTxtRpTitle)
            val txtReportDescription = requireView().findViewById<EditText>(R.id.editTxtDescription)

            val report = Report(
                LoginUtils.getUser(requireContext()),
                txtReportTitle.text.toString(),
                txtReportDescription.text.toString(),
                null,
                txtCategories.text.toString()
            )
            model.newReport(report, imageByteArray, object : INewReport {
                override fun onSuccess(report: Report?) {
                    Toast.makeText(requireContext(), "Se añadió correctamente", Toast.LENGTH_SHORT).show()
                    txtReportTitle.text.clear()
                    txtCategories.text.clear()
                    txtReportDescription.text.clear()
                    reportImage.setImageResource(R.drawable.noimage)
                    //parentFragmentManager.beginTransaction().replace(R.id.flContainer, HomeFragment()).commit();
                    //val bottomNavigationView : BottomNavigationView = view.findViewById(R.id.bottom_navigation);
                    //bottomNavigationView.selectedItemId = R.id.action_home;
                }

                override fun onNoSuccess(code: Int, message: String) {
                    Toast.makeText(requireContext(), "Problem detected $code $message", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(t: Throwable) {
                    Toast.makeText(requireContext(), "Network or server error occurred", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun clickListenerForTakePhoto(): View.OnClickListener {
        return View.OnClickListener {
            val permissionStatus = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            )
            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(requireContext(), "No hay acceso a la cámara", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            } else {
                Log.i("tag", "we have the permission, thanks")
                takePhoto()
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val stream = ByteArrayOutputStream()
                val data = result.data
                val bmp = data?.extras?.get("data") as Bitmap
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
                imageByteArray = stream.toByteArray()

                val bitmap = BitmapFactory.decodeByteArray(
                    imageByteArray, 0,
                    imageByteArray.size
                )
                reportImage.setImageBitmap(bitmap)
            } else {
                Toast.makeText(requireContext(), "Picture was not taken", Toast.LENGTH_SHORT).show()
            }
        }
}
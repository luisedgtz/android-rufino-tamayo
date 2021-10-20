package com.example.parquerufinotamayo.controller

import android.Manifest
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
import android.location.Location
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.parquerufinotamayo.model.Model
import com.example.parquerufinotamayo.model.entities.Category
import com.example.parquerufinotamayo.model.entities.Report
import com.example.parquerufinotamayo.model.repository.responseinterface.IGetAllCategories
import com.example.parquerufinotamayo.model.repository.responseinterface.INewReport
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import dalvik.system.PathClassLoader
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class NewReportFragment : Fragment() {
    companion object{
        private const val CAMERA_PERMISSION_CODE = 100
        private const val MAPS_REQUEST_CODE = 20;
    }

    private lateinit var btnTakePhoto : Button
    private lateinit var btnEndRp : Button
    private lateinit var btnAddLocation : Button
    private lateinit var editTxtRpTitle : EditText
    private lateinit var editTxtDescription : EditText
    private lateinit var reportImage: ImageView
    private lateinit var txtCategories : AutoCompleteTextView

    private lateinit var model : Model
    private lateinit var imageByteArray: ByteArray

    private var lat: String = "25.645055";
    private var lng: String = "-100.329168";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = Model(LoginUtils.getToken(requireContext()))
        imageByteArray = ByteArray(0)
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto)
        btnEndRp = view.findViewById(R.id.btnEndRp)
        btnAddLocation = view.findViewById(R.id.btnAddLocation)
        reportImage = view.findViewById(R.id.reportImage)
        editTxtDescription = view.findViewById(R.id.editTxtDescription)
        editTxtRpTitle = view.findViewById(R.id.editTxtRpTitle)
        txtCategories = view.findViewById(R.id.txtReportSolve)
        val lista = getSpinnerList()

        val adapter = context?.let { ArrayAdapter(it, R.layout.list_item, lista) };
        txtCategories.setAdapter(adapter)

        btnTakePhoto.setOnClickListener(clickListenerForTakePhoto())
        btnAddLocation.setOnClickListener(clickListenerForLocation())
        btnEndRp.setOnClickListener(clickListenerForNewReport())
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
                txtCategories.text.toString(),
                lat,
                lng
            )
            model.newReport(report, imageByteArray, object : INewReport {
                override fun onSuccess(report: Report?) {
                    Toast.makeText(requireContext(), "Se añadió correctamente", Toast.LENGTH_SHORT).show()
                    txtReportTitle.text.clear()
                    txtCategories.text.clear()
                    txtReportDescription.text.clear()
                    reportImage.setImageResource(R.drawable.noimage)

                    val bottomNavigationView : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
                    bottomNavigationView.selectedItemId = R.id.action_home
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

    private fun clickListenerForLocation(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lng", lng)
            startActivityForResult(intent, MAPS_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MAPS_REQUEST_CODE &&  resultCode == Activity.RESULT_OK) {
            if (data != null) {
                lat = data.getStringExtra("Lat").toString()
                lng = data.getStringExtra("Lng").toString()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
package com.example.parquerufinotamayo.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.R
import com.example.parquerufinotamayo.model.Model
import com.example.parquerufinotamayo.model.entities.ReportGet
import com.example.parquerufinotamayo.model.repository.RemoteRepository
import com.example.parquerufinotamayo.model.repository.responseinterface.IGetAllReports
import com.example.parquerufinotamayo.model.repository.responseinterface.IGetReport
import com.example.parquerufinotamayo.model.repository.responseinterface.ISolveReport
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SolveReportFragment : Fragment() {

    private lateinit var btnSolve : Button
    private lateinit var imgSolve: ImageView
    private lateinit var txtID : TextView
    private lateinit var txtTitleSolve : TextView
    private lateinit var txtDescSolve : TextView
    private lateinit var txtDateSolve: TextView
    private lateinit var txtUserSolve : TextView
    private lateinit var txtCatgSolve : TextView
    private lateinit var txtReportSolve : AutoCompleteTextView
    private lateinit var reportGet : ReportGet
    private lateinit var btnLogOut : ImageButton
    var helper = ""
    var helperUserName = ""
    var idList = ArrayList<String>()
    var byteArr = byteArrayOf()

    private lateinit var model : Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_solve_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = Model(LoginUtils.getToken(requireContext()))
        btnSolve = view.findViewById(R.id.btnSolve)
        btnLogOut = view.findViewById(R.id.btnLogOut)
        imgSolve = view.findViewById(R.id.imgSolve)
        txtID = view.findViewById(R.id.txtID)
        txtTitleSolve = view.findViewById(R.id.txtTitleSolve)
        txtDescSolve = view.findViewById(R.id.txtDescSolve)
        txtDateSolve = view.findViewById(R.id.txtDateSolve)
        txtUserSolve = view.findViewById(R.id.txtUserSolve)
        txtCatgSolve = view.findViewById(R.id.txtCatgSolve)
        txtReportSolve = view.findViewById(R.id.txtReportSolve)
        val lista = getSpinnerList()

        val adapter = context?.let { ArrayAdapter(it, R.layout.list_item, lista) };
        txtReportSolve.setAdapter(adapter)

        txtReportSolve.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.i("Test", "Selected")
                helper = idList[p2]
                model.getReport(helper, object : IGetReport {
                    override fun onSuccess(report: ReportGet?) {
                        if(report != null){
                            txtID.text = report._id
                            txtTitleSolve.text = report.title
                            txtDescSolve.text = report.description
                            val sdf = SimpleDateFormat("dd-MM-yyyy")
                            val stringDate = sdf.format(report.creationDate)
                            txtDateSolve.text = stringDate
                            txtUserSolve.text = report.username
                            helperUserName = report.username.toString()
                            txtCatgSolve.text = report.category
                            if (report.images?.isNotEmpty() == true){
                                askForImage(report.images!![0])
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
            }
        }

        btnSolve.setOnClickListener(clickListenerForSolveReport())
        btnLogOut.setOnClickListener(clickOnLogOutButton())
    }

    private fun clickOnLogOutButton(): View.OnClickListener? {
        return View.OnClickListener {
            LoginUtils.deleteInfo(requireContext())
            returnLogin()
        }
    }

    private fun clickListenerForSolveReport(): View.OnClickListener? {
        return View.OnClickListener {
            val report = ReportGet(
                null,
                null,
                getCurrentDate(),
                helperUserName,
                null,
                null,
                ArrayList<String>(),
                null
            )
            model.solveReport(helper, report, byteArr , object : ISolveReport {
                override fun onSuccess(report: ReportGet?) {
                    Toast.makeText(requireContext(), "Se ha resuelto", Toast.LENGTH_SHORT).show()
                    txtID.text = "ID"
                    txtTitleSolve.text = "Título"
                    txtDescSolve.text = "Descripción"
                    txtDateSolve.text = "Fecha de creación"
                    txtUserSolve.text = "Usuario"
                    txtCatgSolve.text = "Categoría"
                    imgSolve.setImageResource(R.drawable.noimage)

                    txtReportSolve.text.clear();
                    helper = ""
                    helperUserName = ""
                    idList.clear()
                    val lista = getSpinnerList()

                    val adapter = context?.let { ArrayAdapter(it, R.layout.list_item, lista) };
                    txtReportSolve.setAdapter(adapter)

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

    private fun getSpinnerList(): ArrayList<String> {
        val reportList = ArrayList<String>()
        model.getAllReports(object : IGetAllReports {
            override fun onSuccess(reports: List<ReportGet>?) {
                if(reports != null){
                    for(report in reports){
                        reportList.add(report.title.toString())
                        idList.add(report._id.toString())
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
        return reportList
    }


    private fun askForImage(photoPath: String) {
        val picasso = RemoteRepository.getPicassoInstance(requireContext(), LoginUtils.getToken(requireContext()))
        val urlForImage = "${LoginUtils.BASE_URL}reports/images/$photoPath"
        picasso.load(urlForImage).into(imgSolve);
    }

    private fun getCurrentDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return sdf.format(Date())
    }

    private fun returnLogin(){
        val mainActivityIntent =
            Intent(requireContext(), LoginActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainActivityIntent)
    }
}
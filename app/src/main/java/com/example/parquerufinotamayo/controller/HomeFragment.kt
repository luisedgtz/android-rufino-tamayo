package com.example.parquerufinotamayo.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.R
import com.example.parquerufinotamayo.model.Model
import com.example.parquerufinotamayo.model.entities.Report
import com.example.parquerufinotamayo.model.entities.ReportGet
import com.example.parquerufinotamayo.model.repository.responseinterface.IGetAllReports

class HomeFragment : Fragment() {

    private lateinit var model : Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = Model(LoginUtils.getToken(requireContext()));
        showReports()
    }

    private fun showReports() {
        model.getAllReports(object : IGetAllReports {
            override fun onSuccess(reports: List<ReportGet>?) {
                if (reports != null) {
                    val rvReports = requireView().findViewById<RecyclerView>(R.id.rvReports)
                    val adapter =
                        ReportsAdapter(reports as MutableList<ReportGet>, object : ReportsAdapter.OnItemClickListener {
                            override fun onItemClick(item: ReportGet) {
                                val intent = Intent(requireContext(), ReportActivity::class.java)
                                intent.putExtra("id", item._id)
                                intent.putExtra("creationDate", item.creationDate)
                                intent.putExtra("attentionDate", item.attentionDate)
                                intent.putExtra("username", item.username)
                                intent.putExtra("title", item.title)
                                intent.putExtra("description", item.description)
                                intent.putExtra("images", item.images?.toTypedArray())
                                intent.putExtra("category", item.category)
                                intent.putExtra("lat", item.lat)
                                intent.putExtra("long", item.long)
                                startActivity(intent)
                            }
                        }, LoginUtils.getToken(requireContext()), requireContext())
                    rvReports.adapter = adapter
                    rvReports.layoutManager = LinearLayoutManager(requireContext())
                }
            }

            override fun onNoSuccess(code: Int, message: String) {
                Toast.makeText(
                    requireContext(),
                    "Problem detected $code $message",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("getProducts", "$code: $message")
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
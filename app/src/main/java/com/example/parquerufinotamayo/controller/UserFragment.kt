package com.example.parquerufinotamayo.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.R
import com.example.parquerufinotamayo.model.Model
import com.example.parquerufinotamayo.model.entities.ReportGet
import com.example.parquerufinotamayo.model.repository.responseinterface.IGetAllReports


class UserFragment : Fragment() {

    private lateinit var model: Model
    private lateinit var tvUsername: TextView
    private lateinit var btnUserSettings : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = Model(LoginUtils.getToken(requireContext()))
        tvUsername = requireView().findViewById(R.id.tvUsername)
        tvUsername.text = LoginUtils.getUser(requireContext())
        Log.i("TYPE", LoginUtils.getUserType(requireContext()))
        btnUserSettings = requireView().findViewById(R.id.btnUserLogout)
        showReports()
        btnUserSettings.setOnClickListener(clickOnUserSettings())
    }

    private fun clickOnUserSettings(): View.OnClickListener? {
        return View.OnClickListener {
            LoginUtils.deleteInfo(requireContext())
            returnLogin()
        }
    }

    private fun showReports() {
        model.getUserReports(LoginUtils.getUser(requireContext()) ,object : IGetAllReports {
            override fun onSuccess(reports: List<ReportGet>?) {
                if (reports != null) {
                    val rvReports = requireView().findViewById<RecyclerView>(R.id.rvUserReports)
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

                    val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            adapter.removeAt(viewHolder.adapterPosition)
                        }
                    }

                    val itemTouchHelper = ItemTouchHelper(swipeHandler)
                    itemTouchHelper.attachToRecyclerView(rvReports)
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

    private fun returnLogin(){
        val mainActivityIntent =
            Intent(requireContext(), LoginActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainActivityIntent)
    }
}
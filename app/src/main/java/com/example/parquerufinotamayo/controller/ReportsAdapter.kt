package com.example.parquerufinotamayo.controller

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.R
import com.example.parquerufinotamayo.model.entities.Report
import com.example.parquerufinotamayo.model.repository.RemoteRepository
import com.example.parquerufinotamayo.LoginUtils.Companion.BASE_URL
import com.example.parquerufinotamayo.model.Model
import com.example.parquerufinotamayo.model.entities.ReportGet
import com.example.parquerufinotamayo.model.repository.responseinterface.IDeleteReport


class ReportsAdapter (
    private val reports: MutableList<ReportGet>,
    private val clickListener: OnItemClickListener,
    private val token: String,
    private val context: Context
) :
    RecyclerView.Adapter<ReportsAdapter.ViewHolder>() {
        interface OnItemClickListener {
            fun onItemClick(item: ReportGet)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView = itemView.findViewById(R.id.tvReportTitle)
            val tvCategory: TextView = itemView.findViewById(R.id.tvCategoryReport)
            val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
            val ivPhoto: ImageView = itemView.findViewById(R.id.ivReportPhoto)
            val ivCheck: ImageView = itemView.findViewById(R.id.ivCheck)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val reportView = inflater.inflate(R.layout.item_report, parent, false)
            return ViewHolder(reportView)
        }

        override fun onBindViewHolder(viewHolder: ReportsAdapter.ViewHolder, position: Int) {
            val report: ReportGet = reports[position]
            viewHolder.tvTitle.text = report.title
            viewHolder.tvCategory.text = report.category
            viewHolder.tvDescription.text = report.description

            if (report.attentionDate == null) {
                viewHolder.ivCheck.visibility = View.GONE
            }

            if (report.images?.isNotEmpty() == true) {
                askForImage(report.images!![0], viewHolder)
            }

            viewHolder.itemView.setOnClickListener {
                clickListener.onItemClick(report)
            }
        }

        private fun askForImage(photoPath: String, viewHolder: ViewHolder) {
            val picasso = RemoteRepository.getPicassoInstance(context, token)
            val urlForImage = "${BASE_URL}reports/images/$photoPath"
            picasso.load(urlForImage).into(viewHolder.ivPhoto);
        }

        override fun getItemCount(): Int {
            return reports.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        fun removeAt(position: Int) {
            val id = reports[position]._id
            val model = Model(LoginUtils.getToken(context))

            if (id != null) {
                model.deleteReport(id, reports[position], object : IDeleteReport {
                    override fun onSuccess(report: ReportGet?) {
                        reports.removeAt(position)
                        notifyItemRemoved(position)
                    }

                    override fun onNoSuccess(code: Int, message: String) {
                        Toast.makeText(
                            context,
                            "Problem detected $code $message",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("DeleteReport", "$code: $message")
                    }

                    override fun onFailure(t: Throwable) {
                        Toast.makeText(
                            context,
                            "Network or server error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("DeleteReport", t.message.toString())
                    }
                })
            }
        }
    }
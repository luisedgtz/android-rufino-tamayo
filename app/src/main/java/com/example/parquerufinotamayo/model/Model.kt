package com.example.parquerufinotamayo.model

import com.example.parquerufinotamayo.model.entities.Category
import com.example.parquerufinotamayo.model.entities.JwtToken
import com.example.parquerufinotamayo.model.entities.Report
import com.example.parquerufinotamayo.model.entities.User
import com.example.parquerufinotamayo.model.repository.RemoteRepository
import com.example.parquerufinotamayo.model.repository.backendinterface.CategoriesApi
import com.example.parquerufinotamayo.model.repository.backendinterface.ReportsApi
import com.example.parquerufinotamayo.model.repository.backendinterface.UsersApi
import com.example.parquerufinotamayo.model.repository.responseinterface.IGetAllCategories
import com.example.parquerufinotamayo.model.repository.responseinterface.ILogin
import com.example.parquerufinotamayo.model.repository.responseinterface.INewReport
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Model(private val token:String) {

    fun getAllCategories(callback: IGetAllCategories) {
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callGetUser = retrofit.create(CategoriesApi::class.java).getAllCategories()
        callGetUser.enqueue(object : Callback<List<Category>?> {
            override fun onResponse(
                call: Call<List<Category>?>,
                response: Response<List<Category>?>
            ) {
                if (response.isSuccessful) callback.onSuccess(response.body())
                else callback.onNoSuccess(response.code(), response.message())
            }

            override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    fun newReport(report: Report, reportPhotoBytes: ByteArray, callback: INewReport){
        val bodyReportPhoto =
            RequestBody.create(MediaType.parse("application/octet-stream"), reportPhotoBytes)
        val partReportPhoto =
            MultipartBody.Part.createFormData("photo", "report.png", bodyReportPhoto)

        val reportAsJson = Gson().toJson(report)
        val reportPart = MultipartBody.Part.createFormData("report", reportAsJson)

        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callAddReport: Call<Report> = if (reportPhotoBytes.isEmpty())
            retrofit.create(ReportsApi::class.java).newReport(reportPart, null)
        else
            retrofit.create(ReportsApi::class.java)
                .newReport(reportPart, partReportPhoto)

        callAddReport.enqueue(object : Callback<Report?> {
            override fun onResponse(call: Call<Report?>, response: Response<Report?>) {
                if (response.isSuccessful) {
                    callback.onSuccess(report)
                } else {
                    val message: String = if (response.errorBody() != null)
                        response.errorBody()!!.string()
                    else
                        response.message()
                    callback.onNoSuccess(response.code(), message)
                }
            }

            override fun onFailure(call: Call<Report?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    fun login(user: User, callback: ILogin){
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callLogin = retrofit.create(UsersApi::class.java).login(user)

        callLogin.enqueue(object : Callback<JwtToken?> {
            override fun onResponse(call: Call<JwtToken?>, response: Response<JwtToken?>) {
                if(response.isSuccessful) callback.onSuccess(response.body())
                else {
                    val message : String = if (response.errorBody() != null)
                        response.errorBody()!!.string()
                    else
                        response.message()
                    callback.onNoSuccess(response.code(), message)
                }
            }

            override fun onFailure(call: Call<JwtToken?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }
}
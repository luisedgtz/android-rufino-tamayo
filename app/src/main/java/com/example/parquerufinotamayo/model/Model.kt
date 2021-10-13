package com.example.parquerufinotamayo.model

import com.example.parquerufinotamayo.model.entities.*
import com.example.parquerufinotamayo.model.repository.RemoteRepository
import com.example.parquerufinotamayo.model.repository.backendinterface.CategoriesApi
import com.example.parquerufinotamayo.model.repository.backendinterface.ReportGetApi
import com.example.parquerufinotamayo.model.repository.backendinterface.ReportsApi
import com.example.parquerufinotamayo.model.repository.backendinterface.UsersApi
import com.example.parquerufinotamayo.model.repository.responseinterface.*
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Model(private val token:String) {

    fun getUserReports(username: String,callback: IGetAllReports) {
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callGetUser = retrofit.create(ReportsApi::class.java).getUserReports(username)
        callGetUser.enqueue(object : Callback<List<ReportGet>?> {
            override fun onResponse(
                call: Call<List<ReportGet>?>,
                response: Response<List<ReportGet>?>
            ) {
                if (response.isSuccessful) callback.onSuccess(response.body())
                else callback.onNoSuccess(response.code(), response.message())
            }

            override fun onFailure(call: Call<List<ReportGet>?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    fun solveReport(_id: String, report: ReportGet, reportPhotoBytes: ByteArray?, callback: ISolveReport){
        val bodyReportPhoto =
            RequestBody.create(MediaType.parse("application/octet-stream"), reportPhotoBytes)
        val partReportPhoto =
            MultipartBody.Part.createFormData("photo", "report.png", bodyReportPhoto)

        val reportAsJson = Gson().toJson(report)
        val reportPart = MultipartBody.Part.createFormData("report", reportAsJson)

        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callAddReport: Call<ReportGet> = retrofit.create(ReportGetApi::class.java).solveReport(_id, reportPart, null)

        callAddReport.enqueue(object : Callback<ReportGet?> {
            override fun onResponse(call: Call<ReportGet?>, response: Response<ReportGet?>) {
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

            override fun onFailure(call: Call<ReportGet?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    fun getAllReports(callback: IGetAllReports) {
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callGetUser = retrofit.create(ReportsApi::class.java).getAllReports()
        callGetUser.enqueue(object : Callback<List<ReportGet>?> {
            override fun onResponse(
                call: Call<List<ReportGet>?>,
                response: Response<List<ReportGet>?>
            ) {
                if (response.isSuccessful) callback.onSuccess(response.body())
                else callback.onNoSuccess(response.code(), response.message())
            }

            override fun onFailure(call: Call<List<ReportGet>?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

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

    fun getReport(_id: String, callback: IGetReport) {
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callGetReport = retrofit.create(ReportGetApi::class.java).getReport(_id)
        callGetReport.enqueue(object : Callback<ReportGet?> {
            override fun onResponse(
                call: Call<ReportGet?>,
                response: Response<ReportGet?>
            ) {
                if (response.isSuccessful) callback.onSuccess(response.body())
                else callback.onNoSuccess(response.code(), response.message())
            }

            override fun onFailure(call: Call<ReportGet?>, t: Throwable) {
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

    fun register(user: User, callback: IRegister){
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callRegister = retrofit.create(UsersApi::class.java).register(user)

        callRegister.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if(response.isSuccessful) callback.onSuccess(response.body())
                else {
                    val message : String = if (response.errorBody() != null)
                        response.errorBody()!!.string()
                    else
                        response.message()
                    callback.onNoSuccess(response.code(), message)
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                callback.onFailure(t)
            }
        })

    }
}
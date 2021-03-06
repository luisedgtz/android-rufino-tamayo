package com.example.parquerufinotamayo.model.repository.backendinterface

import com.example.parquerufinotamayo.model.entities.Report
import com.example.parquerufinotamayo.model.entities.ReportGet
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ReportsApi {
    @Multipart
    @POST ("reports/new")
    fun newReport(
        @Part report: MultipartBody.Part,
        @Part reportPhoto: MultipartBody.Part?
    ): Call<Report>

    @GET("reports/all")
    fun getAllReports(): Call<List<ReportGet>>

    @GET("reports/userdata")
    fun getUserReports(
        @Query("username") username: String?
    ): Call<List<ReportGet>>
}
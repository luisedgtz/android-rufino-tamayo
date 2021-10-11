package com.example.parquerufinotamayo.model.repository.backendinterface

import com.example.parquerufinotamayo.model.entities.Report
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
}
package com.example.parquerufinotamayo.model.repository.backendinterface

import com.example.parquerufinotamayo.model.entities.ReportGet
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ReportGetApi {
    @GET("reports/{id}")
    fun getReport(@Path("id") id: String): Call<ReportGet>

    @Multipart
    @PUT("reports/{id}")
    fun solveReport(@Path("id")
                    id: String,
                    @Part report: MultipartBody.Part,
                    @Part reportPhoto: MultipartBody.Part?)
    : Call<ReportGet>
}
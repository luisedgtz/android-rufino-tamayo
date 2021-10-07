package com.example.parquerufinotamayo.model.repository.backendinterface

import com.example.parquerufinotamayo.model.entities.JwtToken
import com.example.parquerufinotamayo.model.entities.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersApi {
    @POST("users/login")
    fun login(@Body user: User): Call<JwtToken>
}
package com.example.parquerufinotamayo.model.repository.backendinterface

import com.example.parquerufinotamayo.model.entities.Category
import retrofit2.Call
import retrofit2.http.GET

interface CategoriesApi {
    @GET("categories/all")
    fun getAllCategories(): Call<List<Category>>
}
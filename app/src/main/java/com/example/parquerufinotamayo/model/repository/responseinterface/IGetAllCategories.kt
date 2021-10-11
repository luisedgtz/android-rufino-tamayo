package com.example.parquerufinotamayo.model.repository.responseinterface

import com.example.parquerufinotamayo.model.entities.Category

interface IGetAllCategories : IBasicResponse {
    fun onSuccess(categories: List<Category>?)
}
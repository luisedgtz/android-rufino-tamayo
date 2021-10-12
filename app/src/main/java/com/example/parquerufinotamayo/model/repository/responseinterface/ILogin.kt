package com.example.parquerufinotamayo.model.repository.responseinterface

import com.example.parquerufinotamayo.model.entities.JwtToken

interface ILogin: IBasicResponse {
    fun onSuccess(token: JwtToken?)
}
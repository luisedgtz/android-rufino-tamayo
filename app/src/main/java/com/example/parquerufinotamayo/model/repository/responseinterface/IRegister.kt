package com.example.parquerufinotamayo.model.repository.responseinterface

import com.example.parquerufinotamayo.model.entities.JwtToken
import com.example.parquerufinotamayo.model.entities.User

interface IRegister : IBasicResponse{
    fun onSuccess(user: User?)
}
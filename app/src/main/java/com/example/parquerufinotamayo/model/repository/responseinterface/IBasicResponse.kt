package com.example.parquerufinotamayo.model.repository.responseinterface

interface IBasicResponse {
    /**
     * For server error
     */
    fun onNoSuccess(code: Int, message: String)

    /**
     * Some other errors, throwables, etc
     */
    fun onFailure(t: Throwable)
}
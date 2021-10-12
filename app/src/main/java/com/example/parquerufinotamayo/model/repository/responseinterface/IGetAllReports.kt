package com.example.parquerufinotamayo.model.repository.responseinterface

import com.example.parquerufinotamayo.model.entities.Report

interface IGetAllReports : IBasicResponse {
    fun onSuccess(reports: List<Report>?)
}
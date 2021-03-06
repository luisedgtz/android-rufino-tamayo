package com.example.parquerufinotamayo.model.repository.responseinterface

import com.example.parquerufinotamayo.model.entities.ReportGet

interface IGetAllReports : IBasicResponse {
    fun onSuccess(reports: List<ReportGet>?)
}
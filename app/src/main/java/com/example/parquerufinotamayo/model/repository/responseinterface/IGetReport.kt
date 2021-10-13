package com.example.parquerufinotamayo.model.repository.responseinterface

import com.example.parquerufinotamayo.model.entities.ReportGet

interface IGetReport : IBasicResponse {
    fun onSuccess(report: ReportGet?)
}
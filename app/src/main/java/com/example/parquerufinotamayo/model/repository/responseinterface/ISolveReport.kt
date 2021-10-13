package com.example.parquerufinotamayo.model.repository.responseinterface

import com.example.parquerufinotamayo.model.entities.ReportGet

interface ISolveReport: IBasicResponse {
    fun onSuccess(report: ReportGet?)
}
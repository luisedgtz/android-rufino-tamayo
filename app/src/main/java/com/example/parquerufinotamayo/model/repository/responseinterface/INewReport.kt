package com.example.parquerufinotamayo.model.repository.responseinterface

import com.example.parquerufinotamayo.model.entities.Report

interface INewReport: IBasicResponse {
    fun onSuccess(report: Report?)
}
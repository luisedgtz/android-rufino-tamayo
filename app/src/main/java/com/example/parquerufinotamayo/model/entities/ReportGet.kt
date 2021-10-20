package com.example.parquerufinotamayo.model.entities

import java.util.*

class ReportGet(
    var _id: String?,
    var creationDate: Date?,
    var attentionDate: String?,
    var username:String?,
    var title: String?,
    var description: String?,
    var images: List<String>?,
    var category: String?,
    var lat: String?,
    var long: String?){
}
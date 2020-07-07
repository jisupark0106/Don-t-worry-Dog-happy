package com.example.didoo.myapplication.Get

/**
 * Created by didoo on 2018-08-06.
 */
data class ResponseDogInfo (
    var status : String,
    var message : String,
    var dogname: String,
    var dogage: String,
    var dogtype: String,
    var dogweight: String,
    var update_date: String,
    var kcal:String
)

package com.example.didoo.myapplication.Get

/**
 * Created by didoo on 2018-08-18.
 */
data class ResponseDogType (
        var status : String,
        var message : String,
        var typelist: Array<String>
)
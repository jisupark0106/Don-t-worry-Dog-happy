package com.example.didoo.myapplication.Get

/**
 * Created by didoo on 2018-08-11.
 */
data class ResponseMainDog (
        var status : String,
        var message : String,
        var userid: String,
        var doglist: Array<dogList>
)
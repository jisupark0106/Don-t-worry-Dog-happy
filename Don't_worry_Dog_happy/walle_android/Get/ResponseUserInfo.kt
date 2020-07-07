package com.example.didoo.myapplication.Get

/**
 * Created by didoo on 2018-08-09.
 */
data class ResponseUserInfo (
        var status : String,
        var message : String,
        var userid: String,
        var userpwd: String,
        var usermac: String
)

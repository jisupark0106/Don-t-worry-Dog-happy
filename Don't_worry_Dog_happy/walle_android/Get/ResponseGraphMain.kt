package com.example.didoo.myapplication.Get

/**
 * Created by didoo on 2018-08-20.
 */
data class ResponseGraphMain (
        var status : String,
        var message : String,
        var dogid: String,
        var max: String,
        var min: String,
        var wgraphlist: Array<wgraphList>,
        var mgraphlist: Array<mgraphList>
)
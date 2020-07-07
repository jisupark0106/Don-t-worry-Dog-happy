package com.example.didoo.myapplication.Get

/**
 * Created by 2yg on 2017. 11. 20..
 */
data class ContentResponse(
        var pid : Int,
        var title : String,
        var likeNum : Int,
        var uid : Int,
        var name : String,
        var profileImg : String?,
        var date : String,
        var p_isLike : Int
)
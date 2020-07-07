package com.example.didoo.myapplication

import com.example.didoo.myapplication.Get.*
import com.example.didoo.myapplication.Post.*
import com.example.didoo.retrofittest.LoginPost
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by 2yg on 2017. 11. 20..
 */
interface NetworkService {
    //회원 가입
    @POST("userinfo/signin")
    fun signin(@Body signinPost : SigninPost): Call<SigninResponse>
            // @Part profileImg: MultipartBody.Part): Call<SignResponse>

    //로그인
    @POST("userinfo/login")
    fun login(@Body loginPost : LoginPost): Call<LoginResponse>

    //강아지 종류조회
    @GET("userinfo/sttype")
    fun dogtype():Call<ResponseDogType>

    //강아지 등록
    @POST("userinfo/doginfo")
    fun inputdog( @Body doginfo : DogInfoPost) : Call<DoginfoResponse>

    //maindog 화면 정보 불러오기
    @GET("userinfo/maindog")
    fun maindog(
            @Header("userid") userid : String) : Call<ResponseMainDog>

    @GET("momentum/wgraph")
    fun graphmain(
            @Header("did") did : Int) : Call<ResponseGraphMain>


    //강아지 정보 불러오기 J
    @GET("userinfo/showdoginfo")
    fun showdoginfo(
            @Header("did") did : Int) : Call<ResponseDogInfo>

    //강아지 체중 업데이트
    @POST("users/weightUpdate")
    fun weightupdate(@Body weightInfoPost: WeightInfoPost ): Call<WeightInfoResponse>

    //강아지 삭제 J
    @GET("userinfo/deletedog")
    fun deletedog(@Header("did") did:Int ): Call<DeleteDogResponse>


    //사용자 정보 불러오기 J
    @GET("userinfo/showuserinfo")
    fun showuserinfo(
            @Header("userid") userid : String) : Call<ResponseUserInfo>

    //사용자 삭제 J
    @GET("userinfo/deleteperson")
    fun deleteperson( @Header ("userid")userid:String) : Call<DeleteUserResponse>

    //MacUpdate
    @POST("users/MacUpdate")
    fun macupdate(@Body macUpdatePost: MacUpdatePost): Call<MacUpdateResponse>

    @POST("users/feedUpdate")
    fun feedupdate(@Body feedUpdatePost: FeedUpdatePost ): Call<FeedUpdateResponse>

}

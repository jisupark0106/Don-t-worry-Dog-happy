package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.didoo.myapplication.ApplicationController
import com.example.didoo.myapplication.Get.DeleteUserResponse
import com.example.didoo.myapplication.Get.ResponseUserInfo
import com.example.didoo.myapplication.NetworkService
import com.example.didoo.myapplication.Post.MacUpdatePost
import com.example.didoo.myapplication.Post.MacUpdateResponse
import com.example.didoo.myapplication.Post.WeightInfoPost
import com.example.didoo.myapplication.Post.WeightInfoResponse
import com.example.didoo.myapplication.R
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

/**
 * Created by didoo on 2018-07-31.
 */
class MyPageActivity : AppCompatActivity() {
    var users: TextView? = null
    var userMac: TextView?=null
    var deleteUser: Button? = null
    var logOut:Button?=null
    var macReset:Button?=null
    var bundle: Bundle? = null
    var userid =""
    var setting: SharedPreferences?=null
    var editor:SharedPreferences.Editor?=null
    var wifiMan : WifiManager? = null
    var wifiInf : WifiInfo? = null
    var mac: String = "dfafdf"

    private var networkService: NetworkService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        bundle = intent.extras
        userid = bundle!!.getString("userid")

        initActivity()
        networkService = ApplicationController.instance!!.networkService

        //pwd 보여줘...?
        userinfo()

        //유저 삭제
        deleteUser!!.setOnClickListener {
            deleteusergo()

        }
        //로그아웃
        logOut!!.setOnClickListener{
            editor!!.putString("id","")
            editor!!.putString("PW","")
            editor!!.putBoolean("loginAuto",false)
            editor!!.commit()

            val firstIntent= Intent(applicationContext, LoginSample::class.java)
            firstIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(firstIntent)
        }

        macReset!!.setOnClickListener{
            macAlert()
        }

    }
    fun macAlert(){
        val alert = AlertDialog.Builder(this)
        var macInfo:TextView?=null
        alert.setTitle("Home 공유기 주소변경")
        alert.setMessage("Home wifi 연결 후 등록을 눌러주세요")
        // Builder
        with (alert) {

            setPositiveButton("등록") {

                dialog, whichButton ->
                //showMessage("display the game score or anything!")
                dialog.dismiss()
                wifiMan = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                wifiInf = wifiMan!!.connectionInfo

                val macUpdatePost = networkService!!.macupdate(MacUpdatePost(userid=userid,mac=wifiInf!!.bssid))
                    // ApplicationController.instance!!.makeToast(wifiInf!!.bssid)
                     macUpdatePost.enqueue(object : Callback<MacUpdateResponse> {
                        override fun onResponse(call: Call<MacUpdateResponse>?, response: Response<MacUpdateResponse>?) {
                            if(response!!.isSuccessful){
                                if (response!!.body().message.equals("successful Mac Update")){
                                    //CommonData.loginResponse = response!!.body()
                                    //startActivity(Intent(applicationContext, MainActivity::class.java))
                                    ApplicationController.instance!!.makeToast("MAC 업데이트 성공")

                                }
                            }
                        }
                        override fun onFailure(call: Call<MacUpdateResponse>?, t: Throwable?) {
                            ApplicationController.instance!!.makeToast("통신 상태를 확인해주세요")
                        }
                    })
                }

            setNegativeButton("취소") {
                dialog, whichButton ->
                //showMessage("Close the game or anything!")
                dialog.dismiss()
            }
        }

        // Dialog
        val dialog = alert.create()

        dialog.show()
    }
    fun deleteusergo(){

        val deleteuserinfo = networkService!!.deleteperson(userid)
        deleteuserinfo.enqueue(object : Callback<DeleteUserResponse> {
            override fun onResponse(call: retrofit2.Call<DeleteUserResponse>?, response: Response<DeleteUserResponse>?) {
                        if (response!!.isSuccessful) {

                            if(response!!.body().message.equals("person delete")) {

                        ApplicationController.instance!!.makeToast("삭제완료")
                        val firstIntent= Intent(applicationContext, LoginSample::class.java)
                        finish()
                        startActivity(firstIntent)
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<DeleteUserResponse>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("실패")
            }
        })
    }
    fun userinfo(){

        val getuserinfo = networkService!!.showuserinfo(userid)
        getuserinfo.enqueue(object : Callback<ResponseUserInfo> {
            override fun onResponse(call: retrofit2.Call<ResponseUserInfo>?, response: Response<ResponseUserInfo>?) {
                Log.v("aa","독")
                if (response!!.isSuccessful) {

                    if(response!!.body().message.equals("usersearch success")) {

                        users!!.text = response!!.body().userid
                        userMac!!.text = response!!.body().usermac
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<ResponseUserInfo>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("실패")
            }
        })
    }
    fun initActivity(){
        users=findViewById(R.id.user_id)
        userMac=findViewById(R.id.user_mac)
        deleteUser=findViewById(R.id.deleteuser)
        logOut=findViewById(R.id.logout)
        macReset=findViewById(R.id.macset)
        setting=getSharedPreferences("setting",0)
        editor=setting!!.edit()


    }
}

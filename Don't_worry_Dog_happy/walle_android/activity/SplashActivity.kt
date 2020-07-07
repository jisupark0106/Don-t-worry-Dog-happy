package activity

import activity.DogInfoActivity
import activity.LoginSample
import activity.MaindogActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.example.didoo.myapplication.ApplicationController
import com.example.didoo.myapplication.Post.LoginResponse
import com.example.didoo.myapplication.R
import com.example.didoo.retrofittest.LoginPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.example.didoo.myapplication.NetworkService


class SplashActivity : AppCompatActivity() {

    var setting: SharedPreferences?=null
    //var editor: SharedPreferences.Editor?=null
    var mDelayHandler: Handler? = null
    val SPLASH_DELAY: Long = 2000 //3 seconds
    private var networkService: NetworkService? = null


    internal val maindogRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val mainIntent=Intent(applicationContext, MaindogActivity::class.java)
            mainIntent.putExtra("userid",setting!!.getString("id",""))
            startActivity(mainIntent)
            finish()
        }
    }
    internal val loginRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val LoginIntent=Intent(applicationContext, LoginSample::class.java)
            startActivity(LoginIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        setting=getSharedPreferences("setting",0)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        networkService = ApplicationController.instance!!.networkService


           if(setting!!.getBoolean("loginAuto",true)){
               login()
           }
           else{

               gotologin()
           }




    }
    fun gotologin(){

        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(loginRunnable, SPLASH_DELAY)


    }
    fun login(){
        val loginResponse = networkService!!.login(LoginPost(setting!!.getString("id",""), setting!!.getString("PW","")))
        //ApplicationController.instance!!.makeToast("!!!!!!!!!!!!!!!!!!!!")
        loginResponse.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {

                if(response!!.isSuccessful){
                    if (response!!.body().message.equals("login success")){
                        //CommonData.loginResponse = response!!.body()
                        //startActivity(Intent(applicationContext, MainActivity::class.java))
                        ApplicationController.instance!!.makeToast("로그인 성공")


                        mDelayHandler = Handler()

                        //Navigate with delay
                        mDelayHandler!!.postDelayed(maindogRunnable, SPLASH_DELAY)


                    }else{
                        ApplicationController.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })
    }
}
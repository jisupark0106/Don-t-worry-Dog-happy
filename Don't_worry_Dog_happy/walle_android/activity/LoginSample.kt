package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import com.example.didoo.myapplication.ApplicationController
import com.example.didoo.myapplication.NetworkService
import com.example.didoo.myapplication.Post.LoginResponse
import com.example.didoo.myapplication.R
import com.example.didoo.retrofittest.LoginPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by didoo on 2018-08-06.
 */
class LoginSample : AppCompatActivity(), View.OnClickListener {

    var loginId : EditText? =null
    var loginPassword: EditText? = null
    var loginSign: Button? =null
    var loginLogin : Button? = null
    var loginAuto: CheckBox?=null
    var setting: SharedPreferences?=null
    var editor:SharedPreferences.Editor?=null
    private var networkService: NetworkService? = null
    private val TAG : String = "LOG::Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initActivity()

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        loginLogin!!.setOnClickListener(this)
        networkService = ApplicationController.instance!!.networkService

        loginSign!!.setOnClickListener {
            val signIntent=Intent(applicationContext, SigninActivity::class.java)
            startActivity(signIntent)
        }
        /*if(setting!!.getBoolean("loginAuto",true)){
            login()
        }*/

    }

    override fun onClick(v: View?) {
        when(v){
            loginLogin->{

                login()

            }
        }
    }

    fun initActivity(){
        loginId=findViewById(R.id.login_id)
        loginPassword=findViewById(R.id.login_password)
        loginLogin=findViewById(R.id.login_login)
        loginSign=findViewById(R.id.login_sign)
        loginAuto=findViewById(R.id.login_auto)
        setting=getSharedPreferences("setting",0)
        editor=setting!!.edit()

        if(setting!!.getBoolean("loginAuto",false)){
            loginId!!.setText(setting!!.getString("id",""))
            loginPassword!!.setText(setting!!.getString("PW",""))
            loginAuto!!.setChecked(true)
        }
    }

    fun login(){
        val loginResponse = networkService!!.login(LoginPost(loginId!!.text.toString(), loginPassword!!.text.toString()))
       // ApplicationController.instance!!.makeToast("!!!!!!!!!!!!!!!!!!!!")
        loginResponse.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {

                if(response!!.isSuccessful){
                    if (response!!.body().message.equals("login success")){
                        //CommonData.loginResponse = response!!.body()
                        //startActivity(Intent(applicationContext, MainActivity::class.java))
                        ApplicationController.instance!!.makeToast("로그인 성공")

                        if(loginAuto!!.isChecked()){
                            editor!!.putString("id",loginId!!.text.toString())
                            editor!!.putString("PW",loginPassword!!.text.toString())
                            editor!!.putBoolean("loginAuto",true)
                            editor!!.commit()
                        }
                        else{
                            editor!!.putString("id","")
                            editor!!.putString("PW","")
                            editor!!.putBoolean("loginAuto",false)
                            editor!!.commit()
                        }

                        val mainIntent=Intent(applicationContext, MaindogActivity::class.java)
                        mainIntent.putExtra("userid",loginId!!.text.toString())
                        startActivity(mainIntent)
                        finish()


                    }else if (response!!.body().message.equals("check information")){
                        ApplicationController.instance!!.makeToast("id, 비밀번호를 확인해주세요")
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })
    }
}

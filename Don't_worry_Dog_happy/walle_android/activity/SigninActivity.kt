package activity

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.didoo.myapplication.ApplicationController
import com.example.didoo.myapplication.NetworkService
import com.example.didoo.myapplication.Post.LoginResponse
import com.example.didoo.myapplication.Post.SigninPost
import com.example.didoo.myapplication.Post.SigninResponse
import com.example.didoo.myapplication.R
import com.example.didoo.retrofittest.LoginPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by didoo on 2018-07-18.
 * WifiManager wifiMan = (WifiManager) this.getSystemService(
Context.WIFI_SERVICE);
WifiInfo wifiInf = wifiMan.getConnectionInfo();
String macAddr = wifiInf.getMacAddress();
 */
class SigninActivity :AppCompatActivity() {

    var signId: EditText? = null
    var signPassword: EditText? = null
    var signPassword2: EditText? = null
    var signSign: Button? = null
    var mac: String = "dfafdf"
    var wifiMan : WifiManager? = null
    var wifiInf : WifiInfo? = null

    private var networkService: NetworkService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        initActivity()
        networkService = ApplicationController.instance!!.networkService

        /*회원가입*/
        signSign!!.setOnClickListener {

            if(signPassword!!.text.toString().equals(signPassword2!!.text.toString())){
                /*연결 아이디 존재 유무 확인-> 회원가입 완료?*/
                val signinResponse = networkService!!.signin(SigninPost(signId!!.text.toString(), signPassword!!.text.toString(), mac))
                signinResponse.enqueue(object : Callback<SigninResponse> {
                    override fun onResponse(call: Call<SigninResponse>?, response: Response<SigninResponse>?) {
                        if(response!!.isSuccessful){
                            if (response!!.body().message.equals("signin success")){
                                //CommonData.loginResponse = response!!.body()
                                //startActivity(Intent(applicationContext, MainActivity::class.java))
                                ApplicationController.instance!!.makeToast("회원가입 성공" +  mac)
                                Log.v("aaaaawifiInfo", wifiInf.toString())
                                val loginIntent= Intent(applicationContext, LoginSample::class.java)
                                startActivity(loginIntent)

                            }else if (response!!.body().message.equals("select different id")){
                                ApplicationController.instance!!.makeToast("다른 아이디를 선택해 주세요")
                                var myint : Intent = intent
                                myint.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                finish()
                                startActivity(myint)
                                overridePendingTransition(0, 0)
                            }
                        }
                    }
                    override fun onFailure(call: Call<SigninResponse>?, t: Throwable?) {
                        ApplicationController.instance!!.makeToast("통신 상태를 확인해주세요")
                    }
                })

            }
            else{
                Toast.makeText(applicationContext,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show()
                signPassword2!!.text.clear()
            }

        }

    }

    fun initActivity(){
        signId=findViewById(R.id.sign_id)
        signPassword=findViewById(R.id.sign_password)
        signPassword2= findViewById(R.id.sign_password2)
        signSign=findViewById(R.id.sign_sign)
        wifiMan = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiInf = wifiMan!!.connectionInfo
        mac = wifiInf!!.bssid
    }

}
package activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.example.didoo.myapplication.R

/**
 * Created by didoo on 2018-07-18.
 */
class LoginActivity : AppCompatActivity(){

    var loginId : EditText? =null
    var loginPassword: EditText? = null
    var loginSign: Button? =null
    var loginLogin : Button? = null
    var mac = "Sgirtnfin"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)




        initActivity()

        /*로그인 정보 전달*/
       /* loginLogin!!.setOnClickListener {

            /*로그인 정보 확인*/

            val mainIntent=Intent(applicationContext, MaindogActivity::class.java)
            startActivity(mainIntent)

        }*/


        /*회원가입 이동*/
        loginSign!!.setOnClickListener {
            val signIntent=Intent(applicationContext, SigninActivity::class.java)
            startActivity(signIntent)
        }



    }
    fun initActivity(){
        loginId=findViewById(R.id.login_id)
        loginPassword=findViewById(R.id.login_password)
        loginLogin=findViewById(R.id.login_login)
        loginSign=findViewById(R.id.login_sign)
    }

//    override fun onTouchEvent(event : MotionEvent): Boolean {
//        val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(currentFocus.windowToken,0)
//        return true
//    }


}
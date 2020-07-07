package activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import ativity.PagerdogActivity
import com.example.didoo.myapplication.ApplicationController
import com.example.didoo.myapplication.Get.ResponseMainDog
import com.example.didoo.myapplication.Get.ResponseUserInfo
import com.example.didoo.myapplication.NetworkService
import com.example.didoo.myapplication.R
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by didoo on 2018-07-18.
 */


class MaindogActivity : AppCompatActivity(){


    var myInfo : Button? = null
    var infoPager: ViewPager?=null
    var adddog: Button?=null

    var doglist: Array<String>? = null

    var bundle: Bundle? = null
    var userid =""

    private var networkService: NetworkService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maindog)


        bundle = intent.extras
        userid = bundle!!.getString("userid")

        initActivity()
        adddog!!.bringToFront()
        networkService = ApplicationController.instance!!.networkService

        //처음에 maindog불러오는 부분
        maindog()

//        // Initialize a list of string values
//        val alphabets = listOf("1","2","3")//주인이 가진 강아지 수 가져와서
//
//        // Initialize a new pager adapter instance with list
//        val adapter = PagerdogActivity(alphabets)
//
//        // Finally, data bind the view pager widget with pager adapter
//        infoPager!!.adapter = adapter



        /*내정보 조회*/
        myInfo!!.setOnClickListener{
            val mypageIntent=Intent(applicationContext, MyPageActivity::class.java)
            mypageIntent.putExtra("userid",userid)
            //finish()
            startActivity(mypageIntent)

        }
        adddog!!.setOnClickListener{
            val adddogIntent=Intent(applicationContext, InputDogInfoActivity::class.java)
            adddogIntent.putExtra("userid",userid)

            startActivity(adddogIntent)

        }
    }

    fun maindog(){

        val maindog = networkService!!.maindog(userid)
        maindog.enqueue(object : Callback<ResponseMainDog> {
            override fun onResponse(call: retrofit2.Call<ResponseMainDog>?, response: Response<ResponseMainDog>?) {
                Log.v("aa","독")
                if (response!!.isSuccessful) {

                    if(response!!.body().message.equals("usersearch success")) {


                        val adapter = PagerdogActivity(response!!.body().doglist,userid)
                        infoPager!!.adapter = adapter

                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<ResponseMainDog>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("실패")
            }
        })
    }

    fun initActivity(){
        infoPager=findViewById(R.id.infopager)
        myInfo=findViewById(R.id.myinfo)
        adddog=findViewById(R.id.adddog)
    }
}


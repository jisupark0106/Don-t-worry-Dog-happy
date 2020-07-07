package activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.didoo.myapplication.ApplicationController
import com.example.didoo.myapplication.Get.ResponseDogType
import com.example.didoo.myapplication.Get.dogList
import com.example.didoo.myapplication.Get.dogType
import com.example.didoo.myapplication.NetworkService
import com.example.didoo.myapplication.Post.*
import com.example.didoo.myapplication.R
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by didoo on 2018-07-31.
 */
class InputDogInfoActivity : AppCompatActivity() {
    var bundle: Bundle? = null
    var gotomain :Button?=null
    var spinner : Spinner?=null
    //var typename: Array<String>?=null
    var dtype:Int?=null
    //var dtype:Array<String>?=null
    var dogname:EditText?=null
    var dogage:EditText?=null
    var dogweight:EditText?=null
    var userid =""
    private var networkService: NetworkService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inputdoginfo)
        networkService = ApplicationController.instance!!.networkService
        bundle = intent.extras
        userid = bundle!!.getString("userid")

       // dtypeq=arrayOf("a","b","c")
        init()
        val dogtypeq=networkService!!.dogtype()
        dogtypeq.enqueue(object: Callback<ResponseDogType> {
            override fun onResponse(call: retrofit2.Call<ResponseDogType>?, response: Response<ResponseDogType>?) {

                if (response!!.isSuccessful) {

                    if (response!!.body().message.equals("dogtype send success")) {

                        //typename=response!!.body().typelist

                        if (spinner != null) {

                            spinnerarray(response!!.body().typelist)


                        }
                        /*for(i in 0 until response!!.body().typelist!!.size)
                            typename!![i]=response!!.body().typelist!![i]*/
                        // Log.v("typetypetypetypetype", typename!![0])
                        //ApplicationController.instance!!.makeToast(typename!![0].type)
                        /*for(i in 0 until typename!!.size)
                            dtypeq!![i]=typename!![i].type*/
                        //ApplicationController.instance!!.makeToast(dtype!![0])'\



                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<ResponseDogType>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("실패")
            }
        })


        //init()

        /*if (spinner != null) {

            //Log.v("typetypetypetypetype", typename!![0])

            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,typename!!)

            //spinner!!.adapter = arrayAdapter
            spinner!!.adapter=arrayAdapter

            spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id:Long) {


                    //Toast.makeText(this@MainActivity, getString(R.string.selected_item) + " " + personNames[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
*/


        // Initializing a String Array
        //

        /*for(i in typename!!.indices){
            typelist!!.add(typename!![i].type)
        }*/
        gotomain!!.setOnClickListener {
          if(dogname.toString().isEmpty()){

                  }
            else{
              registerdog()
          }


        }
    }

    fun spinnerarray(typename: Array<String>){

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,typename)

        //spinner!!.adapter = arrayAdapter
        spinner!!.adapter=arrayAdapter

        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id:Long) {
                dtype=position+1

                //Toast.makeText(this@MainActivity, getString(R.string.selected_item) + " " + personNames[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

    }
    fun init(){
        spinner = findViewById(R.id.dog_type)
        gotomain= findViewById(R.id.gotomain)
        dogname=findViewById(R.id.dog_name)
        dogage=findViewById(R.id.dog_age)
        dogweight=findViewById(R.id.dog_weight)

    }
    fun registerdog(){
        val registerResponse = networkService!!.inputdog(DogInfoPost(dname = dogname!!.text.toString(),dage = dogage!!.text.toString().toInt(),curweight = dogweight!!.text.toString().toFloat(),userid = userid,stid = dtype!!))
                registerResponse.enqueue(object : Callback<DoginfoResponse> {
            override fun onResponse(call: Call<DoginfoResponse>?, response: Response<DoginfoResponse>?) {
                if(response!!.isSuccessful){
                    if (response!!.body().message.equals("dog join")){
                        //CommonData.loginResponse = response!!.body()
                        //startActivity(Intent(applicationContext, MainActivity::class.java))
                        ApplicationController.instance!!.makeToast("강아지 가입 성공")
                        val GotoMainIntent= Intent(applicationContext, MaindogActivity::class.java)
                        GotoMainIntent.putExtra("userid",userid)
                        finish()
                        startActivity(GotoMainIntent)

                    }else if (response!!.body().message.equals("select different name")){
                        ApplicationController.instance!!.makeToast("다른 이름을 선택해 주세요")
                       /* var myint : Intent = intent
                        myint.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        finish()
                        startActivity(myint)
                        overridePendingTransition(0, 0)*/
                    }
                }
            }
            override fun onFailure(call: Call<DoginfoResponse>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })
    }
}
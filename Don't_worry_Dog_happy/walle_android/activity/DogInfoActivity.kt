package activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.didoo.myapplication.R

import com.example.didoo.myapplication.ApplicationController
import com.example.didoo.myapplication.Get.DeleteDogResponse
import com.example.didoo.myapplication.Get.ResponseDogInfo
import com.example.didoo.myapplication.NetworkService
import com.example.didoo.myapplication.Post.*
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
/**
 * Created by didoo on 2018-07-25.
 */
class  DogInfoActivity : AppCompatActivity() {


    private var networkService: NetworkService? = null
    var dogName: TextView? = null
    var dogAge: TextView? = null
    var dogType: TextView? = null
    var dogWeight: TextView? = null
    var updateDate: TextView? = null
    var updateWeight: Button? = null
    var editInfo: Button? = null
    var deleteDog: Button? = null
    //var did: Int=5
    var userid=""
    var updatefeed:Button?=null
    var showkcal:TextView?=null

//    val infointent = getIntent()!!
//    val extras = infointent.extras!!
//
//    val did = extras.getInt("dogid")

    var bundle: Bundle? = null

    var did: Int = 1
    //var did = bundle!!.getInt("dogid")
    //val did:Int = intent.getIntExtra("dogid")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doginfo)
        bundle = intent.extras
        did = bundle!!.getInt("dogid")
        userid = bundle!!.getString("userid")
        networkService = ApplicationController.instance!!.networkService

        Log.v("bbbbbbbbbbbbbb", did.toString())

        initActivity()

/*강아지 정보 불러오기*/
        Doginfo()


/*강아지 체중 입력하기*/
        updateWeight!!.setOnClickListener {
            displayAlert()
        }



/*강아지 삭제하기*/
        deleteDog!!.setOnClickListener {
            deletedoggo()
        }

        updatefeed!!.setOnClickListener {
            feedUpdate()
        }


    }
    fun feedUpdate(){
        val alert = AlertDialog.Builder(this)
        var feedInfo:EditText?=null
        alert.setTitle("사료정보를 입력하세요")
        alert.setMessage("100g 당 ")
        // Builder
        with (alert) {

            // Add any  input field here
            feedInfo=EditText(context)
            feedInfo!!.hint="407"
            // weightInfo!!.inputType = InputType.TYPE_CLASS_NUMBER

            setPositiveButton("OK") {

                dialog, whichButton ->
                //showMessage("display the game score or anything!")
                dialog.dismiss()
                if (feedInfo!!.text.toString().isEmpty()){

                }
                //var weightinfo=weightInfo!!.text.toString()
                else {

                    val sendfeedinfo = networkService!!.feedupdate(FeedUpdatePost(dId = did, kcal = Math.ceil(100.0/feedInfo!!.text.toString().toDouble()*100)/100))
                    sendfeedinfo.enqueue(object : Callback<FeedUpdateResponse> {
                        override fun onResponse(call: Call<FeedUpdateResponse>?, response: Response<FeedUpdateResponse>?) {
                            if(response!!.isSuccessful){
                                if (response!!.body().message.equals("successful gram Update")){
                                    //CommonData.loginResponse = response!!.body()
                                    //startActivity(Intent(applicationContext, MainActivity::class.java))
                                    ApplicationController.instance!!.makeToast("사료정보 등록")

                                }
                            }
                        }
                        override fun onFailure(call: Call<FeedUpdateResponse>?, t: Throwable?) {
                            ApplicationController.instance!!.makeToast("통신 상태를 확인해주세요")
                        }
                    })
                }


            }

            setNegativeButton("NO") {
                dialog, whichButton ->
                //showMessage("Close the game or anything!")
                dialog.dismiss()
            }
        }

        // Dialog
        val dialog = alert.create()
        dialog.setView(feedInfo)
        dialog.show()
    }

    fun displayAlert(){
        val alert = AlertDialog.Builder(this)
        var weightInfo:EditText?=null
        alert.setTitle("체중을 입력해주세요")
        // Builder
        with (alert) {

            // Add any  input field here
            weightInfo=EditText(context)
            weightInfo!!.hint="3.5"
           // weightInfo!!.inputType = InputType.TYPE_CLASS_NUMBER

            setPositiveButton("OK") {
                dialog, whichButton ->
                //showMessage("display the game score or anything!")
                dialog.dismiss()
                if (weightInfo!!.text.toString().isEmpty()){

                }
                //var weightinfo=weightInfo!!.text.toString()
                else {
                    val sendweightinfo = networkService!!.weightupdate(WeightInfoPost(dId = did,weight = weightInfo!!.text.toString().toFloat()))
                    sendweightinfo.enqueue(object : Callback<WeightInfoResponse> {
                        override fun onResponse(call: Call<WeightInfoResponse>?, response: Response<WeightInfoResponse>?) {
                            if(response!!.isSuccessful){
                                if (response!!.body().message.equals("successful weight Update")){
                                    //CommonData.loginResponse = response!!.body()
                                    //startActivity(Intent(applicationContext, MainActivity::class.java))
                                    ApplicationController.instance!!.makeToast("체중업데이트 성공")

                                }
                            }
                        }
                        override fun onFailure(call: Call<WeightInfoResponse>?, t: Throwable?) {
                            ApplicationController.instance!!.makeToast("통신 상태를 확인해주세요")
                        }
                    })
                }


            }

            setNegativeButton("NO") {
                dialog, whichButton ->
                //showMessage("Close the game or anything!")
                dialog.dismiss()
            }
        }

        // Dialog
        val dialog = alert.create()
        dialog.setView( weightInfo)
        dialog.show()
    }
    /*fun updateWeight(){
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.popup_updateweight,null)

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        val weightinfo = view.findViewById<EditText>(R.id.weight_info)
        val sendweight = view.findViewById<Button>(R.id.send_weight)

        sendweight.setOnClickListener {

            val sendweightinfo = networkService!!.weightupdate(WeightInfoPost(dId = did,weight = weightinfo!!.text.toString().toFloat()))
            sendweightinfo.enqueue(object : Callback<WeightInfoResponse> {
                override fun onResponse(call: Call<WeightInfoResponse>?, response: Response<WeightInfoResponse>?) {
                    if(response!!.isSuccessful){
                        if (response!!.body().message.equals("successful weight Update")){
                            //CommonData.loginResponse = response!!.body()
                            //startActivity(Intent(applicationContext, MainActivity::class.java))
                            ApplicationController.instance!!.makeToast("체중업데이트 성공")
                            finish()

                        }
                    }
                }
                override fun onFailure(call: Call<WeightInfoResponse>?, t: Throwable?) {
                    ApplicationController.instance!!.makeToast("통신 상태를 확인해주세요")
                }
            })
        }







    }*/
    fun deletedoggo() {

        val deletedoginfo = networkService!!.deletedog(did)
        deletedoginfo.enqueue(object : Callback<DeleteDogResponse> {
            override fun onResponse(call: retrofit2.Call<DeleteDogResponse>?, response: Response<DeleteDogResponse>?) {
                if (response!!.isSuccessful) {

                    if (response!!.body().message.equals("delete dog")) {

                        ApplicationController.instance!!.makeToast("삭제완료")
                        val mainIntent = Intent(applicationContext, MaindogActivity::class.java)
                        mainIntent.putExtra("userid",userid)
                        finish()
                        startActivity(mainIntent)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<DeleteDogResponse>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("실패")
            }
        })
    }

    fun Doginfo() {

        val getdoginfo = networkService!!.showdoginfo(did)
        getdoginfo.enqueue(object : Callback<ResponseDogInfo> {
            override fun onResponse(call: retrofit2.Call<ResponseDogInfo>?, response: Response<ResponseDogInfo>?) {
                Log.v("aa", "독")
                if (response!!.isSuccessful) {

                    if (response!!.body().message.equals("dogsearch success")) {

                        dogName!!.text = response!!.body().dogname
                        dogAge!!.text = response!!.body().dogage
                        dogType!!.text = response!!.body().dogtype
                        dogWeight!!.text = response!!.body().dogweight
                        updateDate!!.text = response!!.body().update_date
                        showkcal!!.text=response!!.body().kcal
                    }

                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseDogInfo>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("실패")
            }
        })
    }


    fun initActivity() {
        dogName = findViewById(R.id.dogname)
        dogAge = findViewById(R.id.dogage)
        dogType = findViewById(R.id.dogtype)
        dogWeight = findViewById(R.id.dogweight)
        deleteDog = findViewById(R.id.deletedog)
        updateDate = findViewById(R.id.update_date)
        updateWeight = findViewById(R.id.update_weight)
        updatefeed=findViewById(R.id.update_kcal)
        showkcal=findViewById(R.id.perkcal)


    }
}
package activity

import activity.WgraphFragment
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.didoo.myapplication.ApplicationController
import com.example.didoo.myapplication.Get.*
import com.example.didoo.myapplication.NetworkService
import com.example.didoo.myapplication.R
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by didoo on 2018-07-31.
 */
class GraphMainActivity : AppCompatActivity() {

    var userid=""
    var bundle: Bundle? = null

    var did: Int = 3
    private var networkService: NetworkService? = null
    var graphList:Array<wgraphList>? = null
    var emptyMgraph:Array<mgraphList>? = arrayOf (mgraphList (1, 2, 0, " "))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphmain)
        bundle = intent.extras
        did = bundle!!.getInt("dogid")
        networkService = ApplicationController.instance!!.networkService

        graphmain()

//        var viewPager : ViewPager = findViewById(R.id.viewPager)
//        viewPager.adapter = pagerAdapter(supportFragmentManager)


//            viewPager.adapter = viewAdapter
        //viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab))
//            tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListen

//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                    if (tab != null) {
//                        tab.setIcon(resources.getDrawable(R.drawable.zzanggu1))
//                    }
//                }
//
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    viewPager.currentItem=tab!!.position
//                    tab.setIcon(resources.getDrawable(R.drawable.zzanggu2))
//                }
//            })
    }

    fun graphmain(){

        val graphmain = networkService!!.graphmain(did)
        graphmain.enqueue(object : Callback<ResponseGraphMain> {
            override fun onResponse(call: retrofit2.Call<ResponseGraphMain>?, response: Response<ResponseGraphMain>?) {
                Log.v("aa","독")
                if (response!!.isSuccessful) {

                    if(response!!.body().message.equals("get graph success")) {

                        //Log.v("ffffffffffffff", response!!.body().message)
                        //Log.v("ffffffffffffff", response!!.body().dogid)
                        //Log.v("ffffffffffffff", response!!.body().wgraphlist[0].weight.toString())
                        //ApplicationController.instance!!.makeToast(response!!.body().wgraphlist[0].weight.toString())
                        /*for(i in 0 until response!!.body().wgraphlist.size-1)
                         graphList!![i]=response!!.body().wgraphlist[i].weight.toString().toFloat()*/

                       // Log.v("ffffffffffffff", graphList!![0].weight.toString())
                       // ApplicationController.instance!!.makeToast(response!!.body().dogid)

                        var viewPager: ViewPager = findViewById(R.id.viewPager)
                        viewPager.adapter = pagerAdapter(supportFragmentManager, did, response!!.body().min.toFloat(), response!!.body().max.toFloat(), response!!.body().wgraphlist, response!!.body().mgraphlist)


                    }

                    else if(response!!.body().message.equals("mgraph is empty")){


                        var viewPager : ViewPager = findViewById(R.id.viewPager)
                        viewPager.adapter = pagerAdapter(supportFragmentManager, did, response!!.body().min.toFloat(), response!!.body().max.toFloat(),response!!.body().wgraphlist, emptyMgraph!!)

                    }

                }
            }
            override fun onFailure(call: retrofit2.Call<ResponseGraphMain>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("실패")
            }
        })
    }


    private class pagerAdapter(fm: android.support.v4.app.FragmentManager, did: Int, min: Float, max: Float, wgraphList: Array<wgraphList>, mgraphList: Array<mgraphList>) :  FragmentStatePagerAdapter(fm){


        var dogId = did
        var wgraph = wgraphList
        var mgraph = mgraphList

        var minweight=min
        var maxweight=max

        override fun getItem(position: Int): Fragment {
            when(position){
                1-> {
                    var firstTab : MgraphFragment?= MgraphFragment()
                    val bundle = android.os.Bundle()
                    bundle.putInt("dogId", dogId)
                    bundle.putSerializable("mgraphList", mgraph)
                    firstTab!!.arguments = bundle
                    return firstTab
                }
                0-> {
                    var secondTab : WgraphFragment?= WgraphFragment()
                    val bundle = android.os.Bundle()
                    bundle.putInt("dogId", dogId)
                    bundle.putFloat("minWeight",minweight)
                    bundle.putFloat("maxWeight",maxweight)
                    ApplicationController.instance!!.makeToast(wgraph[0].weight.toString())
                    //Log.v("ffffffffffffff", graph[0].weight.toString())
                    bundle.putSerializable("wgraphList", wgraph)
                    secondTab!!.arguments = bundle
                    return secondTab

                }
            }
            return WgraphFragment()
        }

        override fun getCount(): Int {
            return 2
            //
        }
    }
}
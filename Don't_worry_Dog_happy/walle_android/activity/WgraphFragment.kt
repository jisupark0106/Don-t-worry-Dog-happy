package activity

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.view.LayoutInflater

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.didoo.myapplication.Get.dogList
import com.example.didoo.myapplication.Get.wgraphList
import com.example.didoo.myapplication.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

/**
 * Created by didoo on 2018-07-31.
 */
class WgraphFragment()  : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v= inflater!!.inflate(R.layout.fragment_wgraph,container,false) //레이아웃을 붙이는 과정
        var wgraph : Array<wgraphList>? = null
        var minweight : Float?=null
        var maxweight : Float?=null

        if(arguments!=null){
            wgraph = arguments.getSerializable("wgraphList") as Array<wgraphList>
            minweight = arguments.getFloat("minWeight")
            maxweight = arguments.getFloat("maxWeight")
        }

        val lineChart : LineChart = v.findViewById(R.id.lineChart) as LineChart


        //Log.v("ffffffffffffff", wgraph!![0].weight.toString())
        val listData = ArrayList<Entry>()

        for (i in 0 until wgraph!!.size) {

           listData.add(Entry(i.toFloat(), wgraph!![(wgraph!!.size-1)-i].weight))
        }


       // listData.add(Entry(1f, 3f))
       // listData.add(Entry(0f, 2.7f))
//        listData.add(Entry(1f, 3f))
//        listData.add(Entry(2f, 5f))
//        listData.add(Entry(3f, 2.1f))
//        listData.add(Entry(4f, 4f))
//        listData.add(Entry(5f, 6f))


        val upper_limit = LimitLine(maxweight!!, "max")
        upper_limit.lineWidth = 2f
        upper_limit.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        upper_limit.textSize = 10f

        val under_limit = LimitLine(minweight!!, "min")
        under_limit.lineWidth = 2f
        under_limit.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        under_limit.textSize = 10f

        val leftAxis = lineChart.getAxisLeft()
        leftAxis.removeAllLimitLines() // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(upper_limit)
        leftAxis.addLimitLine(under_limit)
        leftAxis.setAxisMaxValue(maxweight!!+10f)
        leftAxis.setAxisMinValue(0f)
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
       // leftAxis.setDrawZeroLine(true)
        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true)




        lineChart.getAxisRight().setEnabled(false)

        val lineDataSet = LineDataSet(listData, "weight")

        lineDataSet.color = ContextCompat.getColor(v.context, R.color.colorAccent)
        lineDataSet.valueTextColor = ContextCompat.getColor(v.context, android.R.color.white)



        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate() //refresh

        return v

    }

}
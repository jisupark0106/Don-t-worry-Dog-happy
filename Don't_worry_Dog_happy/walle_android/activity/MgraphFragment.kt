package activity

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.widget.TextView
import com.example.didoo.myapplication.Get.dogList
import com.example.didoo.myapplication.Get.mgraphList
import com.example.didoo.myapplication.Get.wgraphList
import com.example.didoo.myapplication.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


/**
 * Created by didoo on 2018-07-31.
 *
 */
class MgraphFragment : Fragment() {


    /*fun MgraphFragment(){

    }*/
    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {

        val v= inflater!!.inflate(R.layout.fragment_mgraph,container,false) //레이아웃을 붙이는 과정

        var mgraph : Array<mgraphList>? = null
        if(arguments!=null){

            mgraph = arguments.getSerializable("mgraphList") as Array<mgraphList>
        }

        val lineChart : LineChart = v.findViewById(R.id.linechart)



        val listData = ArrayList<Entry>()
        for (i in 0 until mgraph!!.size) {

            listData.add(Entry(i.toFloat(), mgraph[(mgraph!!.size - 1) - i].momentum.toFloat()))
        }
//        listData.add(Entry(0f, 200f))
//        listData.add(Entry(1f, 20f))
//        listData.add(Entry(2f, 30f))
//        listData.add(Entry(3f, 10f))
//        listData.add(Entry(4f, 100f))
//        listData.add(Entry(5f, 20f))

        val leftAxis = lineChart.getAxisLeft()
        leftAxis.removeAllLimitLines() // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMinValue(0f)


        val lineDataSet = LineDataSet(listData, "momentum")
        lineDataSet.color = ContextCompat.getColor(v.context, R.color.colorAccent)
        lineDataSet.valueTextColor = ContextCompat.getColor(v.context, android.R.color.white)

        val lineData = LineData(lineDataSet)

        lineChart.data = lineData
        lineChart.invalidate() //refresh

        return v

    }


}
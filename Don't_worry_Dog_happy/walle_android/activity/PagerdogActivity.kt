package ativity

import activity.DogInfoActivity
import activity.GraphMainActivity
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.didoo.myapplication.Get.dogList
import com.example.didoo.myapplication.R

/**
 * Created by didoo on 2018-07-25.
 */
class PagerdogActivity (private val list: Array<dogList>,val userid:String) : PagerAdapter() {


    var dogId : Int? = null


    override fun isViewFromObject(v: View, `object`: Any): Boolean {
        // Return the current view
        return v === `object` as View
    }


    override fun getCount(): Int {
        // Count the items and return it
        return list.count()
    }

    override fun startUpdate(container: ViewGroup?) {
        super.startUpdate(container)

  

    }
    override fun instantiateItem(parent: ViewGroup?, position: Int): Any {
        dogId = list[position].dId

        Log.v("aaaaaaaaaaaa",dogId.toString())
        // Get the view from pager page layout
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.fragment_maindog,parent,false)

        // Get the widgets reference from layout
        val dogimage: ImageView = view.findViewById(R.id.dogimage)
        val dogInfo: Button = view.findViewById(R.id.doginfos)
        val fragment_dogname: TextView = view.findViewById(R.id.fragment_dogname)

        // Set the text views text
        fragment_dogname.text = list[position].dName
        Log.v("aaaaaaaaaaaa",list[position].dName)

        dogInfo!!.setOnClickListener {

            val graphIntent=Intent(view.context, GraphMainActivity::class.java)
            graphIntent.putExtra("dogid", list[position].dId)
            view.context.startActivity(graphIntent)

        }

        dogimage!!.setOnClickListener {
            //Toast.makeText(view.context,tvLower.text.toString(), Toast.LENGTH_SHORT).show()
            //did처리
            val dogInfoIntent = Intent(view.context, DogInfoActivity::class.java)
            dogInfoIntent.putExtra("dogid", list[position].dId)
            dogInfoIntent.putExtra("userid",userid)
            view.context.startActivity(dogInfoIntent)


        }

        // Set the layout background color

        // Add the view to the parent
        parent?.addView(view)

        // Return the view
        return view
    }

    override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any) {
        // Remove the view from view group specified position
        parent.removeView(`object` as View)
    }

}
package br.com.cantinho.bestroute

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.cantinho.bestroute.models.Rider

import kotlinx.android.synthetic.main.activity_riders.*
import java.lang.Boolean.TRUE

class RidersActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var listOfRiders = ArrayList<Rider>()
    var adapter: RidersAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riders)
        setSupportActionBar(toolbar)

        // load riders
        loadRiders(context = this.applicationContext)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun loadRiders(context: Context) {
        recyclerView = findViewById(R.id.recycleView)
        listOfRiders.clear()
        listOfRiders.add(Rider("Samir Clone", "Virtus", R.drawable.samirclone_30, latitude = -7.216386399999999, longitude =  -35.914598494742776))
        listOfRiders.add(Rider("Caynan", "Sandros Bar", R.drawable.caynan_30, latitude = -7.244865, longitude =  -35.876935))
        listOfRiders.add(Rider("Bruno", "Residencial Hermione", R.drawable.bruno_30, latitude = -7.241235, longitude =  -35.893387))
        listOfRiders.add(Rider("Guilherme", "TMSC", R.drawable.guilherme_30, latitude = -7.221092, longitude =  -35.887743))
        listOfRiders.add(Rider("Sampaio", "Bar do Cuzcuz", R.drawable.sampaio_30, latitude = -7.222778, longitude =  -35.877847))
        listOfRiders.add(Rider("Batista", "Taverna", R.drawable.batista_30, latitude = -7.236935, longitude =  -35.890429))
//        listOfRiders.add(Rider("Car 01", "Car 01 is awesome!", R.mipmap.car01, false))
//        listOfRiders.add(Car("Car 02", "Car 02 is awesome!", R.mipmap.car02, true))
//        listOfRiders.add(Car("Car 03", "Car 03 is awesome!", R.mipmap.car03, false))
//        listOfRiders.add(Car("Car 04", "Car 04 is awesome!", R.mipmap.car04, false))
//        listOfRiders.add(Car("Car 05", "Car 05 is awesome!", R.mipmap.car05, true))
//        listOfRiders.add(Car("Car 06", "Car 06 is awesome!", R.mipmap.car06, false))
//        listOfRiders.add(Car("Car 07", "Car 07 is awesome!", R.mipmap.car07, true))
//        listOfRiders.add(Car("Car 08", "Car 08 is awesome!", R.mipmap.car08, true))
//        listOfRiders.add(Car("Car 09", "Car 09 is awesome!", R.mipmap.car09, false))
//        listOfRiders.add(Car("Car 10", "Car 10 is awesome!", R.mipmap.car10, true))
        adapter = RidersAdapter(context, listOfRiders)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    companion object {
        val TYPE_DEFAULT = 1
        val TYPE_EXPENSIVE = 2
    }

    inner class RidersAdapter(private val context: Context, private var listOfRiders: ArrayList<Rider>) :
        RecyclerView.Adapter<RidersAdapter.ViewHolder>() {



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(context)
            return when (viewType) {
                TYPE_DEFAULT -> DefaultViewHolder(inflater.inflate(R.layout.rider_item, parent, false))
                TYPE_EXPENSIVE -> ExpensiveViewHolder(inflater.inflate(R.layout.constraint_rider_item, parent, false))
                else -> DefaultViewHolder(inflater.inflate(R.layout.rider_item, parent, false))
            }
        }

        override fun getItemCount(): Int {
            return listOfRiders.size
        }

        override fun getItemViewType(position: Int): Int {
            val comparable = listOfRiders[position]
            return when(comparable.isCatch) {
                TRUE -> TYPE_EXPENSIVE
                else -> TYPE_DEFAULT
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val car = listOfRiders[position]
            if(holder is ExpensiveViewHolder) {

            }
            holder.name.text = car.name
            holder.description.text = car.description
            holder.image.setImageResource(car.image!!)
            holder.image.setOnClickListener(View.OnClickListener {
//                add(position)
                val intent = Intent(context, RiderInfoActivity::class.java)
                intent.putExtra("name", car.name)
                intent.putExtra("description", car.description)
                intent.putExtra("image", car.image!!)
                intent.putExtra("isExpensive", car.isCatch)
                context.startActivity(intent)
            })
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal val name = itemView.findViewById<TextView>(R.id.tvName)
            internal val description = itemView.findViewById<TextView>(R.id.tvDescription)
            internal val image = itemView.findViewById<ImageView>(R.id.ivImage)
        }

        inner class DefaultViewHolder(itemView: View) : ViewHolder(itemView)

        inner class ExpensiveViewHolder(itemView: View) : ViewHolder(itemView)

    }

}

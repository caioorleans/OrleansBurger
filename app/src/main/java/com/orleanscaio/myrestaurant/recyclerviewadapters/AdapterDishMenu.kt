package com.orleanscaio.myrestaurant.recyclerviewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orleanscaio.myrestaurant.R
import com.orleanscaio.myrestaurant.dish.Dish

class AdapterDishMenu(context:Context, dishes:ArrayList<Dish>): RecyclerView.Adapter<AdapterDishMenu.MyViewHolder>() {

    private var context: Context
    private var dishes: ArrayList<Dish>
    init {
        this.context = context
        this.dishes = dishes
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDishMenu.MyViewHolder {
        //This is where the layout is inflated (givin a look to our rows)
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.card_dish_menu, parent, false)

        return AdapterDishMenu.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterDishMenu.MyViewHolder, position: Int) {
        //assign values to the views created
        holder.nameTextView.text = dishes[position].name
        holder.priceTextView.text = buildString {
            append("R$ ")
            append(dishes[position].cost.toString())
        }

        val hours:Int = dishes[position].timeToPrepare/60
        val minutes:Int = dishes[position].timeToPrepare%60
        holder.preparationTimeTextView.text = buildString {
            append(hours)
            append(" h ")
            append(minutes)
            append(" m")
        }
    }

    override fun getItemCount(): Int {
        //Counts how many items we have
        return dishes.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var imageView: ImageView
        lateinit var nameTextView: TextView
        lateinit var priceTextView: TextView
        lateinit var preparationTimeTextView: TextView
        init {
            imageView = itemView.findViewById(R.id.dish_image)
            nameTextView = itemView.findViewById(R.id.dish_name)
            priceTextView = itemView.findViewById(R.id.dish_cost)
            preparationTimeTextView = itemView.findViewById(R.id.dish_preparation_time)
        }

    }
}
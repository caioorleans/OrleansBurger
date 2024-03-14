package com.orleanscaio.myrestaurant.recyclerviewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orleanscaio.myrestaurant.R
import com.orleanscaio.myrestaurant.dish.Dish
import java.io.IOException

class AdapterDishMenu(context:Context,
                      dishes:ArrayList<Dish>,
                      recyclerInterface: DishMenuAdapterInterface)
    : RecyclerView.Adapter<AdapterDishMenu.MyViewHolder>() {

    private var recyclerViewInterface: DishMenuAdapterInterface

    private var context: Context
    private var dishes: ArrayList<Dish>
    init {
        this.context = context
        this.dishes = dishes
        this.recyclerViewInterface = recyclerInterface
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        //This is where the layout is inflated (givin a look to our rows)
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.card_dish_menu, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //assign values to the views created
        holder.nameTextView.text = dishes[position].name
        holder.priceTextView.text = buildString {
            append("R$ ")
            append("%.2f".format(dishes[position].cost))
        }

        val hours:Int = dishes[position].timeToPrepare/60
        val minutes:Int = dishes[position].timeToPrepare%60
        holder.preparationTimeTextView.text = buildString {
            append(hours)
            append(" h ")
            append(minutes)
            append(" m")
        }

        try {
            val image= context.assets.open(dishes[position].imageUri).readBytes()
            Glide.with(context).load(image)
                .placeholder(R.drawable.baseline_restaurant_menu_24).centerCrop().into(holder.imageView)
        }catch (exception:IOException){
            Glide.with(context).load(R.drawable.baseline_sentiment_very_dissatisfied_24)
                .centerCrop().into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION)
                recyclerViewInterface.onItemClick(dishes[position])
        }

    }

    override fun getItemCount(): Int {
        //Counts how many items we have
        return dishes.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageView: ImageView
        var nameTextView: TextView
        var priceTextView: TextView
        var preparationTimeTextView: TextView
        init {
            imageView = itemView.findViewById(R.id.dish_image)
            nameTextView = itemView.findViewById(R.id.dish_name)
            priceTextView = itemView.findViewById(R.id.dish_cost)
            preparationTimeTextView = itemView.findViewById(R.id.dish_preparation_time)
        }

    }
}
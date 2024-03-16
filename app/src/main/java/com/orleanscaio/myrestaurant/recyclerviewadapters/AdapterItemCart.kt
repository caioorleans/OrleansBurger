package com.orleanscaio.myrestaurant.recyclerviewadapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orleanscaio.myrestaurant.R
import com.orleanscaio.myrestaurant.cart.CartItem
import java.io.IOException

class AdapterItemCart(
    val context: Context,
    private val cart: ArrayList<CartItem>,
    private var recycler: ItemCartAdapterInterface):
    RecyclerView.Adapter<AdapterItemCart.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterItemCart.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.card_item_cart, parent, false)

        return AdapterItemCart.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterItemCart.MyViewHolder, position: Int) {
        holder.nameTextView.text = cart[position].dish.name

        try {
            val image= context.assets.open(cart[position].dish.imageUri).readBytes()
            Glide.with(context).load(image)
                .placeholder(R.drawable.baseline_restaurant_menu_24).centerCrop()
                .into(holder.imageView)
        }catch (exception: IOException){
            Glide.with(context).load(R.drawable.baseline_sentiment_very_dissatisfied_24)
                .centerCrop().into(holder.imageView)
        }

        holder.totalTextView.text =
            context.getString(R.string.currency,
                "%.2f".format(cart[position].numberOfDishes * cart[position].dish.cost))
        holder.quantityTextView.text =
            context.getString(R.string.card_cart_item_quantity, cart[position].numberOfDishes.toString())

        holder.observationsTextView.text = cart[position].observations

        holder.editView.setOnClickListener { recycler.onClickEdit(cart[position].dish)}

        holder.deleteView.setOnClickListener { AlertDialog.Builder(context, R.style.MyAlertDialog)
            .setTitle("Confirmação de exclusão")
            .setMessage("Tem certeza que deseja excluir esse ítem do carrinho?")
            .setPositiveButton(android.R.string.ok){ _, _ ->recycler.onClickDelete(cart[position].dish) }
            .setNegativeButton(android.R.string.cancel){ _, _ -> null}
            .show() }
    }

    override fun getItemCount(): Int {
        return cart.size
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageView: ImageView
        var nameTextView: TextView
        var totalTextView:TextView
        var quantityTextView: TextView
        var observationsTextView:TextView
        var deleteView: TextView
        var editView: TextView

        init {
            imageView = itemView.findViewById(R.id.card_cart_image)
            nameTextView = itemView.findViewById(R.id.card_cart_dish_name)
            totalTextView = itemView.findViewById(R.id.card_cart_item_total_value)
            quantityTextView = itemView.findViewById(R.id.card_cart_dish_quantity)
            observationsTextView = itemView.findViewById(R.id.card_cart_dish_observations)
            deleteView = itemView.findViewById(R.id.card_cart_item_exclude)
            editView = itemView.findViewById(R.id.card_cart_item_edit)

        }
    }
}
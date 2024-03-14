package com.orleanscaio.myrestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orleanscaio.myrestaurant.cart.CartItem
import com.orleanscaio.myrestaurant.cart.CartPreferences
import com.orleanscaio.myrestaurant.databinding.ActivityCartBinding
import com.orleanscaio.myrestaurant.dish.Dish
import com.orleanscaio.myrestaurant.recyclerviewadapters.AdapterDishMenu
import com.orleanscaio.myrestaurant.recyclerviewadapters.AdapterItemCart
import com.orleanscaio.myrestaurant.recyclerviewadapters.ItemCartAdapterInterface

class CartActivity : AppCompatActivity(), ItemCartAdapterInterface {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cart: ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cart = ArrayList(CartPreferences.loadCart(this))

        populateRecyclerViewCart(cart)

        blockButtonIfCartEmpty()
        binding.buttonBuy.setOnClickListener { buy() }

        sumTotalCost()
    }

    override fun onResume() {
        super.onResume()
        cart = ArrayList(CartPreferences.loadCart(this))

        populateRecyclerViewCart(cart)

        blockButtonIfCartEmpty()
        binding.buttonBuy.setOnClickListener { buy() }

        sumTotalCost()
    }
    override fun onClickEdit(dish: Dish) {
        val intent = Intent(this,DishDetailsActivity::class.java)
        intent.putExtra("dish", dish)
        startActivity(intent)
    }

    override fun onClickDelete(dish: Dish) {
        cart.removeIf { it.dish.id == dish.id }
        CartPreferences.saveCart(this,cart)
        populateRecyclerViewCart(cart)
        blockButtonIfCartEmpty()
        sumTotalCost()
    }

    private fun sumTotalCost(){
        val total = cart.sumOf { it.numberOfDishes * it.dish.cost.toDouble() }.toFloat()
        binding.cartTotal.text = this.getString(R.string.currency, "%.2f".format(total))
    }
    private fun blockButtonIfCartEmpty(){
        if(cart.isEmpty()){
            binding.buttonBuy.isEnabled = false
        }
    }
    private fun populateRecyclerViewCart(cart: ArrayList<CartItem>){
        val recyclerView: RecyclerView = findViewById(R.id.cart_recycler_view)
        val adapterDishMenu = AdapterItemCart(this, cart, this)
        recyclerView.adapter = adapterDishMenu
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun buy(){
        CartPreferences.deleteCart(this)
        Toast.makeText(this, getString(R.string.sucess_buy), Toast.LENGTH_LONG).show()
        finish()
    }
}
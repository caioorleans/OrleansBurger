package com.orleanscaio.myrestaurant

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
import com.orleanscaio.myrestaurant.recyclerviewadapters.AdapterDishMenu
import com.orleanscaio.myrestaurant.recyclerviewadapters.AdapterItemCart

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cart = ArrayList(CartPreferences.loadCart(this))

        populateRecyclerViewCart(cart)

        if(cart.isEmpty()){
            binding.buttonBuy.isEnabled = false
        }
        binding.buttonBuy.setOnClickListener { buy() }

        val total = cart.sumOf { it.numberOfDishes * it.dish.cost.toDouble() }.toFloat()
        binding.cartTotal.text = this.getString(R.string.currency, "%.2f".format(total))
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun populateRecyclerViewCart(cart: ArrayList<CartItem>){
        val recyclerView: RecyclerView = findViewById(R.id.cart_recycler_view)
        val adapterDishMenu = AdapterItemCart(this, cart)
        recyclerView.adapter = adapterDishMenu
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun buy(){
        CartPreferences.deleteCart(this)
        Toast.makeText(this, getString(R.string.sucess_buy), Toast.LENGTH_LONG).show()
        finish()
    }
}
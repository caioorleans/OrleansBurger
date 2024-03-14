package com.orleanscaio.myrestaurant

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val recyclerView: RecyclerView = findViewById(R.id.cart_recycler_view)
        val adapterDishMenu = AdapterItemCart(this, ArrayList(CartPreferences.loadCart(this)))
        recyclerView.adapter = adapterDishMenu
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
package com.orleanscaio.myrestaurant

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.orleanscaio.myrestaurant.cart.CartItem
import com.orleanscaio.myrestaurant.cart.CartPreferences
import com.orleanscaio.myrestaurant.databinding.ActivityDishDetailsBinding
import com.orleanscaio.myrestaurant.dish.Dish
import java.io.IOException

class DishDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDishDetailsBinding
    private lateinit var dish:Dish
    private lateinit var cart:MutableList<CartItem>
    private var dishQuantity = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dish = intent.getSerializableExtra("dish") as Dish
        cart = CartPreferences.loadCart(this)

        bindActivityElements(dish)
    }

    private fun bindActivityElements(dish: Dish){
        try {
            val image= assets.open(dish.imageUri).readBytes()
            Glide.with(this).load(image)
                .centerCrop().into(binding.dishDetailsDishImage)
        }
        catch (ex:IOException){
            Glide.with(this).load(R.drawable.baseline_sentiment_very_dissatisfied_24)
                .centerCrop().into(binding.dishDetailsDishImage)
        }

        binding.dishDetailsDishName.text = dish.name
        binding.dishDetailsDishIngredients.text = dish.ingredients
        binding.dishDetailsTimeToPrepare.text = getString(R.string.minutes, dish.timeToPrepare.toString())
        binding.dishDetailsPrice.text = getString(R.string.currency, dish.cost.toString())

        binding.dishDetailsTimeToPrepare.setTextColor(
            if(dish.timeToPrepare > 20)
                ContextCompat.getColor(this, R.color.red)
            else
                ContextCompat.getColor(this,R.color.green)
        )

        val cartItem = cart.find { it.dish.id == dish.id }

        if (cartItem != null){
            dishQuantity = cartItem.numberOfDishes
            binding.dishDetailsDishObservations.text = SpannableStringBuilder(cartItem.observations)
        }

        if(dishQuantity <= 1)
            binding.dishDetailsDecrease.isEnabled = false
        binding.dishDetailsQuantity.text = dishQuantity.toString();

        binding.dishDetailsIncrease.setOnClickListener { increase() }
        binding.dishDetailsDecrease.setOnClickListener { decrease() }

        updateTotal()

        binding.dishDetailsAddToCart.setOnClickListener { addToCart() }
    }

    private fun increase(){
        dishQuantity++
        binding.dishDetailsQuantity.text = dishQuantity.toString();
        updateTotal()
        if(dishQuantity > 1)
            binding.dishDetailsDecrease.isEnabled = true
    }

    private fun decrease(){
        if(dishQuantity >1){
            dishQuantity--
            binding.dishDetailsQuantity.text = dishQuantity.toString();
            updateTotal()

            if(dishQuantity <= 1)
                binding.dishDetailsDecrease.isEnabled = false
        }
    }

    private fun updateTotal(){
        val total = dishQuantity * dish.cost
        binding.dishDetailsTotal.text =
            getString(R.string.dish_details_total,
                dishQuantity.toString(), dish.cost.toString(), total.toString())
    }

    private fun addToCart(){
        val cartItem = cart.find { it.dish.id == dish.id }
        if (cartItem != null){
            cartItem.numberOfDishes = dishQuantity
            cartItem.observations = binding.dishDetailsDishObservations.text.toString()
        }
        else{
            val newCartItem = CartItem(
                dish, dishQuantity, binding.dishDetailsDishObservations.text.toString())
            cart.add(newCartItem)
        }
        CartPreferences.saveCart(this, ArrayList(cart))

        finish()
    }
}
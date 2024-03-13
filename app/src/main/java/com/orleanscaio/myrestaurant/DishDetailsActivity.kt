package com.orleanscaio.myrestaurant

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.orleanscaio.myrestaurant.databinding.ActivityDishDetailsBinding
import com.orleanscaio.myrestaurant.dish.Dish
import java.io.IOException

class DishDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDishDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dish = intent.getSerializableExtra("dish") as Dish
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
    }
}
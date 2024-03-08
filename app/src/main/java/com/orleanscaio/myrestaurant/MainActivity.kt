package com.orleanscaio.myrestaurant

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.orleanscaio.myrestaurant.databinding.ActivityWelcomeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orleanscaio.myrestaurant.databinding.ActivityMainBinding
import com.orleanscaio.myrestaurant.dish.Dish
import com.orleanscaio.myrestaurant.dish.DishesXmlParser
import com.orleanscaio.myrestaurant.recyclerviewadapters.AdapterDishMenu

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dishes: ArrayList<Dish>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner = createSpinner(this);

        dishes = DishesXmlParser().parseDishes(R.xml.dishes, this);

        depoisnomeioerraporra(this, dishes, spinner)

        //val imageView:ImageView = findViewById(R.id.imageView)

        //val imageStream = assets.open("batatinhas.jpg").readBytes()
        //Glide.with(this).load(imageStream).into(imageView)

        //populateRecyclerViewMenuDishes(this, dishes)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.item_cart -> {
                Toast.makeText(this, "Item de cart clicado", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun createSpinner(context:Context):Spinner{
        val foodCategorySpinner = findViewById<Spinner>(R.id.food_category_spinner)

        val foodCategories = arrayOf(
            getString(R.string.appetizer),
            getString(R.string.main_course),
            getString(R.string.dessert),
            getString(R.string.drinks),
            getString(R.string.all_meals)
        )

        val arrayAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, foodCategories)

        foodCategorySpinner.adapter = arrayAdapter

        return foodCategorySpinner
    }

    private fun populateRecyclerViewMenuDishes(context: Context, dishes: ArrayList<Dish>){
        val recyclerView:RecyclerView = findViewById(R.id.recycler_view_dishes)
        val adapterDishMenu = AdapterDishMenu(context, dishes)
        recyclerView.adapter = adapterDishMenu
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun depoisnomeioerraporra(context: Context, meals: ArrayList<Dish>, spinner:Spinner){
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var foodFiltered: ArrayList<Dish> = ArrayList()
                when(position){
                    0 -> {
                        for (meal in meals){
                            if (meal.category == "Appetizer") foodFiltered.add(meal)
                        }
                    }
                    1 -> {
                        for (meal in meals){
                            if (meal.category == "Main Course") foodFiltered.add(meal)
                        }
                    }
                    2 -> {
                        for (meal in meals){
                            if (meal.category == "Dessert") foodFiltered.add(meal)
                        }
                    }
                    3 -> {
                        for (meal in meals){
                            if (meal.category == "Drinks") foodFiltered.add(meal)
                        }
                    }
                    else -> {
                        foodFiltered = meals;
                    }
                }
                populateRecyclerViewMenuDishes(context,foodFiltered)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}
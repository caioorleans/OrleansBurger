package com.orleanscaio.myrestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orleanscaio.myrestaurant.databinding.ActivityMainBinding
import com.orleanscaio.myrestaurant.dish.Dish
import com.orleanscaio.myrestaurant.dish.DishesXmlParser
import com.orleanscaio.myrestaurant.recyclerviewadapters.AdapterDishMenu
import com.orleanscaio.myrestaurant.recyclerviewadapters.DishMenuAdapterInterface

class MainActivity : AppCompatActivity(), DishMenuAdapterInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dishes: ArrayList<Dish>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner = createSpinner(this);

        dishes = DishesXmlParser().parseDishes(R.xml.dishes, this);

        selectDishCategory(dishes, spinner)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.item_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onItemClick(dish: Dish) {
        val intent = Intent(this,DishDetailsActivity::class.java)
        intent.putExtra("dish", dish)
        startActivity(intent)
    }

    private fun createSpinner(context:Context):Spinner{
        val foodCategorySpinner = findViewById<Spinner>(R.id.food_category_spinner)

        val foodCategories = arrayOf(
            getString(R.string.all_meals),
            getString(R.string.appetizer),
            getString(R.string.main_course),
            getString(R.string.dessert),
            getString(R.string.drinks)
        )

        val arrayAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, foodCategories)

        foodCategorySpinner.adapter = arrayAdapter

        return foodCategorySpinner
    }

    private fun populateRecyclerViewMenuDishes(dishes: ArrayList<Dish>){
        val recyclerView:RecyclerView = findViewById(R.id.recycler_view_dishes)
        val adapterDishMenu = AdapterDishMenu(this, dishes, this)
        recyclerView.adapter = adapterDishMenu
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun selectDishCategory(dishes: ArrayList<Dish>, spinner:Spinner){
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val foodFiltered: ArrayList<Dish>
                when(position){
                    1 -> {
                        foodFiltered = dishes.filter { it.category ==  "Appetizer"} as ArrayList<Dish>
                    }
                    2 -> {
                        foodFiltered = dishes.filter { it.category ==  "Main Course"} as ArrayList<Dish>
                    }
                    3 -> {
                        foodFiltered = dishes.filter { it.category ==  "Dessert"} as ArrayList<Dish>
                    }
                    4 -> {
                        foodFiltered = dishes.filter { it.category ==  "Drinks"} as ArrayList<Dish>
                    }
                    else -> {
                        foodFiltered = dishes;
                    }
                }
                populateRecyclerViewMenuDishes(foodFiltered)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}
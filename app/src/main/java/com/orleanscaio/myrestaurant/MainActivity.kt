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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orleanscaio.myrestaurant.databinding.ActivityMainBinding
import com.orleanscaio.myrestaurant.dish.Dish
import com.orleanscaio.myrestaurant.dish.DishesXmlParser
import com.orleanscaio.myrestaurant.recyclerviewadapters.AdapterDishMenu
import com.orleanscaio.myrestaurant.recyclerviewadapters.RecyclerViewAdapterInterface

class MainActivity : AppCompatActivity(), RecyclerViewAdapterInterface {

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
                Toast.makeText(this, "Item de cart clicado", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onItemClick(dish: Dish) {
        Toast.makeText(this, dish.name, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,DishDetailsActivity::class.java))
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
                var foodFiltered: ArrayList<Dish> = ArrayList()
                when(position){
                    0 -> {
                        for (dish in dishes){
                            if (dish.category == "Appetizer") foodFiltered.add(dish)
                        }
                    }
                    1 -> {
                        for (dish in dishes){
                            if (dish.category == "Main Course") foodFiltered.add(dish)
                        }
                    }
                    2 -> {
                        for (dish in dishes){
                            if (dish.category == "Dessert") foodFiltered.add(dish)
                        }
                    }
                    3 -> {
                        for (dish in dishes){
                            if (dish.category == "Drinks") foodFiltered.add(dish)
                        }
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
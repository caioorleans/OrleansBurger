package com.orleanscaio.myrestaurant

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnCreateContextMenuListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.orleanscaio.myrestaurant.databinding.ActivityMainBinding
import com.orleanscaio.myrestaurant.dish.DishesXmlParser

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createSpinner(this);
        val dishes = DishesXmlParser().parseDishes(R.xml.dishes, this);
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

    private fun createSpinner(context:Context){
        val foodCategorySpinner = findViewById<Spinner>(R.id.food_category_spinner)

        val foodCategories = arrayOf(
            getString(R.string.appetizer),
            getString(R.string.main_course),
            getString(R.string.dessert),
            getString(R.string.drinks)
        )

        val arrayAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, foodCategories)

        foodCategorySpinner.adapter = arrayAdapter
    }
}
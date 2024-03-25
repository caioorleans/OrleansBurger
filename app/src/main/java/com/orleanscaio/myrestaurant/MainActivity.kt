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
import com.google.gson.Gson
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

        //Cria o spinner para posteriormente ser populado
        val SPINNER = createSpinner(this);

        //carrega todos os pratos a partir de um arquivo xml
        dishes = DishesXmlParser().parseDishes(R.xml.dishes, this);

        //Função responsável por separar os pratos por categoria
        selectDishCategory(dishes, SPINNER)
    }

    //Insere o menu_main_Activity na action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Funciona como um onClickListener para cada item do menu
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

    //Método herdado de DishMenuAdapterInterface
    //Redireciona para a página do prato
    override fun onItemClick(dish: Dish) {
        val INTENT = Intent(this,DishDetailsActivity::class.java)
        val GSON = Gson()
        val JSON = GSON.toJson(dish)
        INTENT.putExtra("dish", JSON)
        startActivity(INTENT)
    }

    //Popula o spinner com as categorias de prato
    private fun createSpinner(context:Context):Spinner{
        val FOOD_CATEGORY_SPINNER = findViewById<Spinner>(R.id.spinner_food_category)

        val FOOD_CATEGORIES = arrayOf(
            getString(R.string.all_meals),
            getString(R.string.appetizer),
            getString(R.string.main_course),
            getString(R.string.dessert),
            getString(R.string.drinks)
        )

        val ARRAY_ADAPTER = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, FOOD_CATEGORIES)

        FOOD_CATEGORY_SPINNER.adapter = ARRAY_ADAPTER

        return FOOD_CATEGORY_SPINNER
    }

    //Popula o recycler view a partir de uma lista de pratos
    private fun populateRecyclerViewMenuDishes(dishes: ArrayList<Dish>){
        val RECYCLER_VIEW:RecyclerView = findViewById(R.id.recycler_view_dishes)
        val ADAPTER_DISH_MENU = AdapterDishMenu(this, dishes, this)
        RECYCLER_VIEW.adapter = ADAPTER_DISH_MENU
        RECYCLER_VIEW.layoutManager = LinearLayoutManager(this)
    }

    //Seleciona os pratos a partir da categoria e os manda para populares o recycler view
    private fun selectDishCategory(dishes: ArrayList<Dish>, spinner:Spinner){
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val FILTERED_FOOD: ArrayList<Dish>
                when(position){
                    1 -> {
                        FILTERED_FOOD = dishes.filter { it.category ==  "Appetizer"} as ArrayList<Dish>
                    }
                    2 -> {
                        FILTERED_FOOD = dishes.filter { it.category ==  "Main Course"} as ArrayList<Dish>
                    }
                    3 -> {
                        FILTERED_FOOD = dishes.filter { it.category ==  "Dessert"} as ArrayList<Dish>
                    }
                    4 -> {
                        FILTERED_FOOD = dishes.filter { it.category ==  "Drinks"} as ArrayList<Dish>
                    }
                    else -> {
                        FILTERED_FOOD = dishes;
                    }
                }
                populateRecyclerViewMenuDishes(FILTERED_FOOD)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}
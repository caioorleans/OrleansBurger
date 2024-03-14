package com.orleanscaio.myrestaurant.recyclerviewadapters

import com.orleanscaio.myrestaurant.dish.Dish

interface DishMenuAdapterInterface {
    fun onItemClick(dish: Dish)
}
package com.orleanscaio.myrestaurant.recyclerviewadapters

import com.orleanscaio.myrestaurant.dish.Dish

interface RecyclerViewAdapterInterface {
    fun onItemClick(dish: Dish)
}
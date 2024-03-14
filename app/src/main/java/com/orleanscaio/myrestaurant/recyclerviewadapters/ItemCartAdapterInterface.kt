package com.orleanscaio.myrestaurant.recyclerviewadapters

import com.orleanscaio.myrestaurant.dish.Dish

interface ItemCartAdapterInterface {

    fun onClickEdit(dish: Dish)
    fun onClickDelete(dish: Dish)
}
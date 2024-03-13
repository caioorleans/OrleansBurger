package com.orleanscaio.myrestaurant.cart

import com.orleanscaio.myrestaurant.dish.Dish

class CartItem (
    val dish: Dish,
    var numberOfDishes: Int,
    var observations: String
)
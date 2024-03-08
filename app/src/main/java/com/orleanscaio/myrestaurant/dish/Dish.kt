package com.orleanscaio.myrestaurant.dish

data class Dish (
    val id: Int,
    val category: String,
    val name: String,
    val ingredients: String,
    val timeToPrepare: Int,
    val cost: Float,
    val imageUri:String
)
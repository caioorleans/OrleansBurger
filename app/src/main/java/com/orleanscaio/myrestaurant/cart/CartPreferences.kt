package com.orleanscaio.myrestaurant.cart

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

object CartPreferences {
    private const val PREFS_NAME = "cart_prefs"
    private const val CART_KEY = "cart"

    fun saveCart(context: Context, cartItems:ArrayList<CartItem>){
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(cartItems)
        editor.putString(CART_KEY, json)
        editor.apply()
    }

    fun loadCart(context: Context):MutableList<CartItem>{
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(CART_KEY, null)
        val type = object : TypeToken<ArrayList<CartItem>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun deleteCart(context:Context){
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(CART_KEY)
        editor.apply()
    }
}
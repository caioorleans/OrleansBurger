package com.orleanscaio.myrestaurant.cart

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

object CartPreferences {
    private const val PREFS_NAME = "cart_prefs"
    private const val CART_KEY = "cart"

    fun saveCart(context: Context, cartItems:ArrayList<CartItem>){
        val SHARED_PREFERENCES = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val EDITOR = SHARED_PREFERENCES.edit()
        val GSON = Gson()
        val JSON = GSON.toJson(cartItems)
        EDITOR.putString(CART_KEY, JSON)
        EDITOR.apply()
    }

    fun loadCart(context: Context):MutableList<CartItem>{
        val SHARED_PREFERENCES = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val GSON = Gson()
        val JSON = SHARED_PREFERENCES.getString(CART_KEY, null)
        val TYPE = object : TypeToken<ArrayList<CartItem>>() {}.type
        return GSON.fromJson(JSON, TYPE) ?: mutableListOf()
    }

    fun deleteCart(context:Context){
        val SHARED_PREFERENCES = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val EDITOR = SHARED_PREFERENCES.edit()
        EDITOR.remove(CART_KEY)
        EDITOR.apply()
    }
}
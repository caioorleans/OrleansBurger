package com.orleanscaio.myrestaurant.dish

import android.content.Context
import org.xmlpull.v1.XmlPullParser
import java.lang.Exception
class DishesXmlParser {

    fun parseDishes(xmlResourceId: Int, context:Context): ArrayList<Dish> {
        val DISHES = ArrayList<Dish>()

        try {
            val parser = context.resources.getXml(xmlResourceId)
            parser.next()
            var eventType = parser.eventType
            var dishId = 0
            var category = ""
            var name = ""
            var ingredients = ""
            var timeToPrepare = 0
            var cost = 0f
            var imageUri = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "id" -> {
                                parser.next()
                                dishId = parser.text.toInt()
                            }
                            "category" -> {
                                parser.next()
                                category = parser.text
                            }
                            "name" -> {
                                parser.next()
                                name = parser.text
                            }
                            "ingredients" -> {
                                parser.next()
                                ingredients = parser.text
                            }
                            "time_to_prepare" -> {
                                parser.next()
                                timeToPrepare = parser.text.toInt()
                            }
                            "cost" -> {
                                parser.next()
                                cost = parser.text.toFloat()
                            }
                            "image_uri" -> {
                                parser.next()
                                imageUri = parser.text
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "dish") {
                            val DISH = Dish(dishId, category, name, ingredients, timeToPrepare, cost, imageUri)
                            DISHES.add(DISH)
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            println(e)
            e.printStackTrace()
        }

        return DISHES
    }

}
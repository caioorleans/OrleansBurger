package com.orleanscaio.myrestaurant

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orleanscaio.myrestaurant.cart.CartItem
import com.orleanscaio.myrestaurant.cart.CartPreferences
import com.orleanscaio.myrestaurant.databinding.ActivityDishDetailsBinding
import com.orleanscaio.myrestaurant.dish.Dish
import java.io.IOException

class DishDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDishDetailsBinding
    private lateinit var dish:Dish
    private lateinit var cart:MutableList<CartItem>
    private var dishQuantity = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: Buscar função para substituir essa que está depreciada
        //Recebe o prato enviado pela página anterior
        val GSON = Gson()
        val JSON_DISH = intent.getStringExtra("dish")
        val TYPE = object : TypeToken<Dish>() {}.type

        dish = GSON.fromJson(JSON_DISH, TYPE)

        //Carrega o carrinho a partir dos shared preferences
        cart = CartPreferences.loadCart(this)

        bindActivityElements(dish)
    }

    //Faz o binding dos elementos da página
    private fun bindActivityElements(dish: Dish){
        //Carrega a imagem
        //Caso a imagem não seja encontrada, vai exibir um simbolo genérico
        try {
            val IMAGE= assets.open(dish.imageUri).readBytes()
            Glide.with(this).load(IMAGE)
                .centerCrop().into(binding.imageDishDetails)
        }
        catch (ex:IOException){
            Glide.with(this).load(R.drawable.baseline_sentiment_very_dissatisfied_24)
                .centerCrop().into(binding.imageDishDetails)
        }

        binding.textDishDetailsDishName.text = dish.name
        binding.textDishDetailsDishIngredients.text = dish.ingredients
        //Formata e seta o tempo de preparo
        binding.textDishDetailsTimeToPrepare.text = getString(R.string.minutes, dish.timeToPrepare.toString())
        //Formata e seta o valor do prato
        binding.textDishDetailsPrice.text = getString(R.string.currency, "%.2f".format(dish.cost))

        //Seta a cor do texto do tempo de preparo, dependendo do tempo
        binding.textDishDetailsTimeToPrepare.setTextColor(
            if(dish.timeToPrepare > 20)
                ContextCompat.getColor(this, R.color.red)
            else
                ContextCompat.getColor(this,R.color.green)
        )

        //procura o prato dentro do carrinho
        val CART_ITEM = cart.find { it.dish.id == dish.id }

        //se o prato já estiver no carrinho
        //atualiza a interface com as informações previamente registradas
        if (CART_ITEM != null){
            dishQuantity = CART_ITEM.numberOfDishes
            binding.editDishDetailsDishObservations.text = SpannableStringBuilder(CART_ITEM.observations)
            binding.buttonDishDetailsAddToCart.text = getString(R.string.update_request)
        }

        //desabilita o botão de diminuir quantidade de item se quantidade for <=1
        if(dishQuantity <= 1)
            binding.buttonDishDetailsDecrease.isEnabled = false
        binding.textDishDetailsQuantity.text = dishQuantity.toString();

        //seta os listeners dos botões de incremento e decremento
        binding.buttonDishDetailsIncrease.setOnClickListener { increase() }
        binding.buttonDishDetailsDecrease.setOnClickListener { decrease() }

        //atualiza o valor total
        updateTotal()

        binding.buttonDishDetailsAddToCart.setOnClickListener { addToCart() }
    }

    private fun increase(){
        dishQuantity++
        binding.textDishDetailsQuantity.text = dishQuantity.toString();
        updateTotal()
        if(dishQuantity > 1)
            binding.buttonDishDetailsDecrease.isEnabled = true
    }

    private fun decrease(){
        if(dishQuantity >1){
            dishQuantity--
            binding.textDishDetailsQuantity.text = dishQuantity.toString();
            updateTotal()

            if(dishQuantity <= 1)
                binding.buttonDishDetailsDecrease.isEnabled = false
        }
    }

    private fun updateTotal(){
        val TOTAL = dishQuantity * dish.cost
        binding.textDishDetailsTotal.text =
            getString(R.string.dish_details_total,
                dishQuantity.toString(), "%.2f".format(dish.cost), "%.2f".format(TOTAL))
    }

    //Adiciona o item ao carrinho ou atualiza as infos caso o item já estivesse no mesmo
    private fun addToCart(){
        val CART_ITEM = cart.find { it.dish.id == dish.id }
        if (CART_ITEM != null){
            CART_ITEM.numberOfDishes = dishQuantity
            CART_ITEM.observations = binding.editDishDetailsDishObservations.text.toString()
            Toast.makeText(this, "Alterações salvas", Toast.LENGTH_SHORT).show()
        }
        else{
            val NEW_CART_ITEM = CartItem(
                dish, dishQuantity, binding.editDishDetailsDishObservations.text.toString())
            cart.add(NEW_CART_ITEM)
            Toast.makeText(this, "Pedido salvo no carrinho", Toast.LENGTH_SHORT).show()
        }
        CartPreferences.saveCart(this, ArrayList(cart))

        finish()
    }
}
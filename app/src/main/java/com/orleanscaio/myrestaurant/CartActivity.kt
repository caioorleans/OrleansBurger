package com.orleanscaio.myrestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orleanscaio.myrestaurant.cart.CartItem
import com.orleanscaio.myrestaurant.cart.CartPreferences
import com.orleanscaio.myrestaurant.databinding.ActivityCartBinding
import com.orleanscaio.myrestaurant.dish.Dish
import com.orleanscaio.myrestaurant.recyclerviewadapters.AdapterItemCart
import com.orleanscaio.myrestaurant.recyclerviewadapters.ItemCartAdapterInterface

class CartActivity : AppCompatActivity(), ItemCartAdapterInterface {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cart: ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Carrega o carrinho
        cart = ArrayList(CartPreferences.loadCart(this))

        //Popula o recycler view com os itens do carrinho
        populateRecyclerViewCart(cart)

        //Bloqueia o botão de finalizar compra se o carrinho estiver vazio
        blockButtonIfCartEmpty()
        //Seta o botão de compra
        binding.buttonBuy.setOnClickListener { buy() }

        //Soma o valor total do carrinho
        sumTotalCost()
    }

    //Necessário para atualizar o carrinho
    override fun onResume() {
        super.onResume()
        cart = ArrayList(CartPreferences.loadCart(this))

        populateRecyclerViewCart(cart)

        blockButtonIfCartEmpty()
        binding.buttonBuy.setOnClickListener { buy() }

        sumTotalCost()
    }

    //Método de ItemCartAdapterInterface para editar o prato selecionado
    override fun onClickEdit(dish: Dish) {
        val INTENT = Intent(this,DishDetailsActivity::class.java)
        INTENT.putExtra("dish", dish)
        startActivity(INTENT)
    }

    //Método de ItemCartAdapterInterface para excluir o prato selecionado
    override fun onClickDelete(dish: Dish) {
        cart.removeIf { it.dish.id == dish.id }
        CartPreferences.saveCart(this,cart)
        populateRecyclerViewCart(cart)
        blockButtonIfCartEmpty()
        sumTotalCost()
    }

    private fun sumTotalCost(){
        val TOTAL = cart.sumOf { it.numberOfDishes * it.dish.cost.toDouble() }.toFloat()
        binding.cartTotal.text = this.getString(R.string.currency, "%.2f".format(TOTAL))
    }
    private fun blockButtonIfCartEmpty(){
        if(cart.isEmpty()){
            binding.buttonBuy.isEnabled = false
        }
    }
    private fun populateRecyclerViewCart(cart: ArrayList<CartItem>){
        val RECYCLER_VIEW: RecyclerView = findViewById(R.id.cart_recycler_view)
        val ADAPTER_ITEM_CART = AdapterItemCart(this, cart, this)
        RECYCLER_VIEW.adapter = ADAPTER_ITEM_CART
        RECYCLER_VIEW.layoutManager = LinearLayoutManager(this)
    }

    private fun buy(){
        //Deleta o carrinho
        CartPreferences.deleteCart(this)
        //Exibe o toast
        Toast.makeText(this, getString(R.string.sucess_buy), Toast.LENGTH_LONG).show()
        //termina a activity
        finish()
    }
}
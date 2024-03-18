package com.orleanscaio.myrestaurant

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.orleanscaio.myrestaurant.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private val HANDLER: Handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Esconde a action bar
        supportActionBar?.hide()

        //Executa as ações somente após uma determinada quantidade de tempo
        HANDLER.postDelayed({
            //Indica que a activity será encerrada após a transição
            finishAfterTransition()
            //Inicia a próxima activity
            startActivity(Intent(this,MainActivity::class.java))
        }, 3000)
    }
}
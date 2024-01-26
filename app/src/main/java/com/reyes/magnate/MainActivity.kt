package com.reyes.magnate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtén referencias a los CardViews
        val cardViewBebidasCalientes = findViewById<CardView>(R.id.cardViewBebidasCalientes)
        val cardViewBebidasFrias = findViewById<CardView>(R.id.cardViewBebidasFrias)
        val cardViewPostres = findViewById<CardView>(R.id.cardViewPostres)

        // Agrega OnClickListener a cada CardView
        cardViewBebidasCalientes.setOnClickListener {
            // Abre la nueva pantalla para Bebidas Calientes
            val intent = Intent(this, BebidasCalientes::class.java)
            startActivity(intent)

            // Muestra un Toast en la actividad actual
            Toast.makeText(this, "Abriendo Calientes", Toast.LENGTH_SHORT).show()
        }

        cardViewBebidasFrias.setOnClickListener {
            // Abre la nueva pantalla para Bebidas Frías
            val intent = Intent(this, BebidasFrias::class.java)
            startActivity(intent)

            // Muestra un Toast en la actividad actual
            Toast.makeText(this, "Abriendo Bebidas Frias", Toast.LENGTH_SHORT).show()
        }

        cardViewPostres.setOnClickListener {
            // Abre la nueva pantalla para Postres
            val intent = Intent(this, Postres::class.java)
            startActivity(intent)

            // Muestra un Toast en la actividad actual
            Toast.makeText(this, "Abriendo Postres", Toast.LENGTH_SHORT).show()
        }
    }

}

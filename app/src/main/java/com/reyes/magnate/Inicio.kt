package com.reyes.magnate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // Obtén referencias a los botones en tu diseño
        val btnRegistrarse: Button = findViewById(R.id.btnRegistrarse)
        val btnIniciarSesion: Button = findViewById(R.id.btnIniciarsesion)

        // Configura el clic del botón "Registrarse"
        btnRegistrarse.setOnClickListener {
            // Crea un Intent para abrir la actividad Registro
            val intent = Intent(this, Registro::class.java)

            // Inicia la actividad Registro
            startActivity(intent)
        }

        // Configura el clic del botón "Iniciar Sesión"
        btnIniciarSesion.setOnClickListener {
            // Crea un Intent para abrir la actividad IniciarSesion
            val intent = Intent(this, IniciarSesion::class.java)

            // Inicia la actividad IniciarSesion
            startActivity(intent)
        }
    }
}

package com.reyes.magnate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IniciarSesion : AppCompatActivity() {
    private lateinit var dbHelper: Registro.DBHelper
    private lateinit var usuarioEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var btnIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)

        dbHelper = Registro.DBHelper(this)

        // Inicializar los EditText y el botón
        usuarioEditText = findViewById(R.id.editTextText)
        passwordEditText = findViewById(R.id.editTextTextPassword)
        btnIngresar = findViewById(R.id.btnIngresar)

        // Configurar el evento de clic del botón de inicio de sesión
        btnIngresar.setOnClickListener {
            // Obtener los datos ingresados por el usuario
            val usuario = usuarioEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Verificar las credenciales del usuario en la base de datos
            if (validarCredenciales(usuario, password)) {
                // Credenciales válidas, mostrar un Toast de éxito
                showToast("Inicio de sesión exitoso")

                // Redirigir a la actividad principal (MainActivity)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cerrar la actividad actual
            } else {
                // Credenciales inválidas, mostrar un Toast de error
                showToast("Usuario o contraseña incorrectos. Inténtalo de nuevo.")
            }
        }
    }

    private fun validarCredenciales(usuario: String, password: String): Boolean {
        // Validar las credenciales en la base de datos
        val db = dbHelper.readableDatabase
        val projection = arrayOf(Usuarios.UserEntry.COLUMN_ID)
        val selection = "${Usuarios.UserEntry.COLUMN_USERNAME} = ? AND ${Usuarios.UserEntry.COLUMN_PASSWORD} = ?"
        val selectionArgs = arrayOf(usuario, password)

        val cursor = db.query(
            Usuarios.UserEntry.TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val resultado = cursor.count > 0

        cursor.close()
        return resultado
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

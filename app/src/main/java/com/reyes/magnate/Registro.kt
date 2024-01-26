package com.reyes.magnate

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Registro : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var usuarioEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registrarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        dbHelper = DBHelper(this)

        // Inicializar los EditText y el botón
        nombreEditText = findViewById(R.id.editTextText)
        apellidoEditText = findViewById(R.id.editTextText2)
        usuarioEditText = findViewById(R.id.editTextText3)
        emailEditText = findViewById(R.id.editTextTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextTextPassword2)
        registrarButton = findViewById(R.id.button2)

        // Configurar el evento de clic del botón de registro
        registrarButton.setOnClickListener {
            // Obtener los datos ingresados por el usuario
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val usuario = usuarioEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Insertar el nuevo usuario en la base de datos
            val registroExitoso = dbHelper.insertarUsuario(nombre, apellido, usuario, email, password)

            // Verificar si el registro fue exitoso
            if (registroExitoso) {
                // Mostrar un Toast de éxito
                showToast("Registro exitoso")

                // Redirigir a la actividad de inicio
                val intent = Intent(this, Inicio::class.java)
                startActivity(intent)
                finish() // Para cerrar la actividad actual
            } else {
                // Mostrar un Toast de error
                showToast("Error al registrar. Inténtalo de nuevo.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // No es necesario para este ejemplo básico.
        }

        fun insertarUsuario(nombre: String, apellido: String, usuario: String, email: String, password: String): Boolean {
            val db = writableDatabase

            // Crear un nuevo mapa de valores, donde las columnas son las claves
            val values = ContentValues().apply {
                put(Usuarios.UserEntry.COLUMN_NOMBRES, nombre)
                put(Usuarios.UserEntry.COLUMN_APELLIDO_P, apellido)
                put(Usuarios.UserEntry.COLUMN_USERNAME, usuario)
                put(Usuarios.UserEntry.COLUMN_CORREO, email)
                put(Usuarios.UserEntry.COLUMN_PASSWORD, password)
            }

            // Insertar la nueva fila
            val newRowId = db.insert(Usuarios.UserEntry.TABLE, null, values)

            // Verificar si la inserción fue exitosa
            return newRowId > -1
        }

        companion object {
            const val DATABASE_NAME = "users.db"
            const val DATABASE_VERSION = 1

            private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${Usuarios.UserEntry.TABLE} (" +
                        "${Usuarios.UserEntry.COLUMN_ID} INTEGER PRIMARY KEY," +
                        "${Usuarios.UserEntry.COLUMN_NOMBRES} TEXT," +
                        "${Usuarios.UserEntry.COLUMN_APELLIDO_P} TEXT," +
                        "${Usuarios.UserEntry.COLUMN_CORREO} TEXT," +
                        "${Usuarios.UserEntry.COLUMN_USERNAME} TEXT," +
                        "${Usuarios.UserEntry.COLUMN_PASSWORD} TEXT)"
        }
    }
}

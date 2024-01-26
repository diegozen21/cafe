package com.reyes.magnate

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.util.*

class BebidasFrias : AppCompatActivity() {

    private lateinit var bebidasFriasList: RecyclerView
    private lateinit var bebidasFriasAdapter: BebidasFriasAdapter
    private val bebidasList: ArrayList<Bebida> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bebidas_frias)

        bebidasFriasList = findViewById(R.id.bebidasFriasList)
        bebidasFriasList.layoutManager = LinearLayoutManager(this)
        bebidasFriasAdapter = BebidasFriasAdapter(bebidasList)
        bebidasFriasList.adapter = bebidasFriasAdapter

        // Llama a la función para cargar las bebidas desde la API
        cargarBebidas()

        bebidasFriasAdapter.setOnItemClickListener(object : BebidasFriasAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Obtener la bebida seleccionada
                val bebidaSeleccionada = bebidasList[position]

                // Redirigir a la nueva pantalla pasando datos, si es necesario
                abrirNuevaPantalla(bebidaSeleccionada)
            }
        })
    }

    private fun abrirNuevaPantalla(bebida: Bebida) {
        val intent = Intent(this, DetalleBebidaFria::class.java)

        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarBebidas() {
        val url = "https://lapatroncita.000webhostapp.com/magnate/obtener_bebidas_frias.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    for (i in 0 until response.length()) {
                        val bebidaObject = response.getJSONObject(i)
                        val nombre = bebidaObject.getString("nombre")
                        val imagenBase64 = bebidaObject.getString("imagen")

                        // Convierte la cadena Base64 a un objeto Bitmap
                        val imagenBitmap = base64ToBitmap(imagenBase64)

                        // Agrega la bebida a la lista
                        bebidasList.add(Bebida(nombre, imagenBitmap))
                    }

                    // Notifica al adaptador que se han agregado bebidas
                    bebidasFriasAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })

        // Agrega la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    private fun base64ToBitmap(base64String: String): Bitmap {
        val imageBytes = Base64.getDecoder().decode(base64String)
        return BitmapFactory.decodeStream(ByteArrayInputStream(imageBytes))
    }

    class BebidasFriasAdapter(private val bebidasList: List<Bebida>) :
        RecyclerView.Adapter<BebidasFriasAdapter.BebidasFriasViewHolder>() {

        private var onItemClickListener: OnItemClickListener? = null

        // Interfaz para manejar clics
        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

        fun setOnItemClickListener(listener: OnItemClickListener) {
            onItemClickListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BebidasFriasViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_bebida_fria, parent, false)
            return BebidasFriasViewHolder(view)
        }

        override fun onBindViewHolder(holder: BebidasFriasViewHolder, position: Int) {
            val bebida = bebidasList[position]
            holder.nombreTextView.text = bebida.nombre
            holder.imagenImageView.setImageBitmap(bebida.imagen)

            // Configurar el click listener para el elemento
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(position)
            }
        }

        override fun getItemCount(): Int {
            return bebidasList.size
        }

        class BebidasFriasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
            val imagenImageView: ImageView = itemView.findViewById(R.id.imagenImageView)
        }
    }

    data class Bebida(val nombre: String, val imagen: Bitmap)
}

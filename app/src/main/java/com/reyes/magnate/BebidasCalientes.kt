package com.reyes.magnate

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.io.ByteArrayInputStream
import java.util.ArrayList
import java.util.Base64

class BebidasCalientes : AppCompatActivity() {

    private lateinit var bebidasCalientesList: RecyclerView
    private lateinit var bebidasCalientesAdapter: BebidasCalientesAdapter
    private val bebidasList: ArrayList<Bebida> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bebidas_calientes)

        bebidasCalientesList = findViewById(R.id.bebidasCalientesList)
        bebidasCalientesList.layoutManager = LinearLayoutManager(this)
        bebidasCalientesAdapter = BebidasCalientesAdapter(bebidasList)
        bebidasCalientesList.adapter = bebidasCalientesAdapter

        // Llama a la función para cargar las bebidas desde la API
        cargarBebidas()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarBebidas() {
        val url = "https://lapatroncita.000webhostapp.com/magnate/obtener_bebidas_calientes.php"

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
                    bebidasCalientesAdapter.notifyDataSetChanged()

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

    class BebidasCalientesAdapter(private val bebidasList: List<Bebida>) :
        RecyclerView.Adapter<BebidasCalientesAdapter.BebidasCalientesViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BebidasCalientesViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_bebida_caliente, parent, false)
            return BebidasCalientesViewHolder(view)
        }

        override fun onBindViewHolder(holder: BebidasCalientesViewHolder, position: Int) {
            val bebida = bebidasList[position]
            holder.nombreTextView.text = bebida.nombre
            holder.imagenImageView.setImageBitmap(bebida.imagen)
        }

        override fun getItemCount(): Int {
            return bebidasList.size
        }

        class BebidasCalientesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
            val imagenImageView: ImageView = itemView.findViewById(R.id.imagenImageView)
        }
    }

    data class Bebida(val nombre: String, val imagen: Bitmap)
}
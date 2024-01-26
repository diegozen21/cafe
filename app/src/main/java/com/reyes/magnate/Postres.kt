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

class Postres : AppCompatActivity() {

    private lateinit var postresList: RecyclerView
    private lateinit var postresAdapter: PostresAdapter
    private val postreList: ArrayList<Postre> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postres)

        postresList = findViewById(R.id.postresList)
        postresList.layoutManager = LinearLayoutManager(this)
        postresAdapter = PostresAdapter(postreList)
        postresList.adapter = postresAdapter

        // Llama a la función para cargar las bebidas desde la API
        cargarPostres()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarPostres() {
        val url = "https://lapatroncita.000webhostapp.com/magnate/obtener_postres.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    for (i in 0 until response.length()) {
                        val postreObject = response.getJSONObject(i)
                        val nombre = postreObject.getString("nombre")
                        val imagenBase64 = postreObject.getString("imagen")

                        // Convierte la cadena Base64 a un objeto Bitmap
                        val imagenBitmap = base64ToBitmap(imagenBase64)

                        // Agrega la bebida a la lista
                        postreList.add(Postre(nombre, imagenBitmap))
                    }

                    // Notifica al adaptador que se han agregado bebidas
                    postresAdapter.notifyDataSetChanged()

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

    class PostresAdapter(private val postreList: List<Postre>) :
        RecyclerView.Adapter<PostresAdapter.PostresViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostresViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_postre, parent, false)
            return PostresViewHolder(view)
        }

        override fun onBindViewHolder(holder: PostresViewHolder, position: Int) {
            val postre = postreList[position]
            holder.nombreTextView.text = postre.nombre
            holder.imagenImageView.setImageBitmap(postre.imagen)
        }

        override fun getItemCount(): Int {
            return postreList.size
        }

        class PostresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
            val imagenImageView: ImageView = itemView.findViewById(R.id.imagenImageView)
        }
    }

    data class Postre(val nombre: String, val imagen: Bitmap)
}
package mx.tecnm.lapaz.tienda

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class ProductosAdapter(var lista:MutableList<Producto>,
                    val clickListener:(Producto)-> Unit): RecyclerView.Adapter<ProductosAdapter.Contenedor>()
{
    class Contenedor(val view: View): RecyclerView.ViewHolder(view) {
        val tvNombre:TextView
        val ivImagen:ImageView
        val tvPrecio:TextView
        init {
            tvNombre = view.findViewById(R.id.tvNombre)
            ivImagen = view.findViewById(R.id.ivImagen)
            tvPrecio = view.findViewById(R.id.tvPrecio)
        }
        fun bind(producto: Producto, clickListener: (Producto) -> Unit) {
            view.setOnClickListener { clickListener(producto)}
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i:Int):Contenedor  {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.renglon_producto, viewGroup, false);
        return (Contenedor(view))
    }

    override fun onBindViewHolder(contenedor:Contenedor, i:Int) {
        contenedor.tvNombre.setText(lista[i].nombre)
        contenedor.tvPrecio.setText(lista[i].precio.toString())
        contenedor.ivImagen.setImageResource(R.drawable.producto)
        val fileName = lista[i].id.toString()+".jpg"
        val file = File(contenedor.view.context.filesDir, fileName )
        val scope = CoroutineScope(Job())
        if (file.exists()) {
            /*scope.launch{
                val bitmap = BitmapFactory.decodeStream(
                    contenedor.view.context.openFileInput(fileName).readBytes().inputStream())
                (contenedor.view.context as Activity).runOnUiThread {
                    contenedor.ivImagen.setImageBitmap(bitmap)
                }
            } */
            scope.launch {
                val bitmap = WebServiceREST.getImage(url + "/storage/imagenes/" + fileName)
                bitmap?.let {
                    contenedor.view.context.openFileOutput(fileName, Context.MODE_PRIVATE)
                        .use { fos ->
                            it.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                        }
                    (contenedor.view.context as Activity).runOnUiThread {
                        contenedor.ivImagen.setImageBitmap(bitmap)
                    }
                }
            }
        }
        else{
            scope.launch {
                val bitmap = WebServiceREST.getImage(url + "/storage/imagenes/" + fileName)
                bitmap?.let {
                    contenedor.view.context.openFileOutput(fileName, Context.MODE_PRIVATE)
                        .use { fos ->
                            it.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                        }
                    (contenedor.view.context as Activity).runOnUiThread {
                        contenedor.ivImagen.setImageBitmap(bitmap)
                    }
                }
            }
        }
        contenedor.bind(lista[i], clickListener)
    }


    override fun getItemCount(): Int {
        return lista.size
    }
}

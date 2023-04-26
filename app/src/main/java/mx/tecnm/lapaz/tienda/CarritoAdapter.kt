package mx.tecnm.lapaz.tienda

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarritoAdapter(var lista:MutableList<Carrito>,
                       val clickListener:(Carrito)-> Unit): RecyclerView.Adapter<CarritoAdapter.Contenedor>()
{
    class Contenedor(val view: View): RecyclerView.ViewHolder(view) {
        val tvNombre:TextView
        val tvPrecio:TextView
        val tvCantidad:TextView
        val tvImporte:TextView
        init {
            tvNombre = view.findViewById(R.id.tvNombre)
            tvPrecio = view.findViewById(R.id.tvPrecio)
            tvCantidad = view.findViewById(R.id.tvCantidad)
            tvImporte = view.findViewById(R.id.tvImporte)
        }
        fun bind(carrito: Carrito, clickListener: (Carrito) -> Unit) {
            view.setOnClickListener { clickListener(carrito)}
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i:Int):Contenedor  {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.renglon_carrito, viewGroup, false);
        return (Contenedor(view))
    }

    override fun onBindViewHolder(contenedor:Contenedor, i:Int) {
        contenedor.tvNombre.setText(lista[i].nombre)
        contenedor.tvPrecio.setText(lista[i].precio.toString())
        contenedor.tvCantidad.setText(lista[i].cantidad.toString())
        contenedor.tvImporte.setText((lista[i].precio*lista[i].cantidad).toString())
        contenedor.bind(lista[i], clickListener)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}
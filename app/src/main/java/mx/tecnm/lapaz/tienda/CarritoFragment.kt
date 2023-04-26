package mx.tecnm.lapaz.tienda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CarritoFragment : Fragment() {
    lateinit var carrito : MutableList<Carrito>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carrito, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scope = CoroutineScope(Job())
        scope.launch {
            val main = (activity as MainActivity)
            var token = ""
            token = ""+main.cliente?.token
            carrito= mutableListOf()
            carrito = Carrito.show(token)

            requireActivity().runOnUiThread {
                val adaptador = CarritoAdapter(carrito, {
                    //mostrarDetalle(it)
                })
                val rvCarrito = view.findViewById<RecyclerView>(R.id.rvCarrito)
                rvCarrito.adapter = adaptador
                val gridLayout =
                    GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                rvCarrito.setLayoutManager(gridLayout)
            }
        }
    }
}
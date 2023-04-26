package mx.tecnm.lapaz.tienda

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.io.Serializable

class ProductosFragment : Fragment() {

    private lateinit var root: View;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_productos, container, false)
        return root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Producto.show()



            requireActivity().runOnUiThread {
                val adaptador = ProductosAdapter(lista, {
                    mostrarDetalle(it)
                })
                val rvProductos = view.findViewById<RecyclerView>(R.id.rvProductos)
                rvProductos.adapter = adaptador
                val gridLayout =
                    GridLayoutManager(context, 8, GridLayoutManager.VERTICAL, false)
                rvProductos.setLayoutManager(gridLayout)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Producto.show()
            requireActivity().runOnUiThread {
                val adaptador = ProductosAdapter(lista, {
                    mostrarDetalle(it)
                })
                val rvProductos = root.findViewById<RecyclerView>(R.id.rvProductos)
                rvProductos.adapter = adaptador
                adaptador.notifyDataSetChanged()
                rvProductos.requestLayout()
            }
        }
    }

    fun mostrarDetalle(producto: Producto){
        val p = Bundle()
        val scope = CoroutineScope(Job())
        scope.launch {
            val lista = Producto.getProducto(producto.id)
            producto.detalle = lista.detalle
            producto.envio = lista.envio
            requireActivity().runOnUiThread {
                p.putSerializable("producto",producto as Serializable)
                findNavController().navigate(R.id.action_productosFragment_to_productoFragment,p)
            }
        }

    }
}
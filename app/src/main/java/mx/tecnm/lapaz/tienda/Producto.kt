package mx.tecnm.lapaz.tienda

import com.google.gson.Gson
import java.io.Serializable

class Producto(
    var id:Int=0,
    var nombre:String="",
    var precio:Float=0f,
    var envio:Float=0f,
    var detalle:String=""
): Serializable {
    companion object {
        suspend fun show(): MutableList<Producto> {
            val datos = WebServiceREST.get(url+"/productos/show")
            val lista = Gson().fromJson<Array<Producto>>(datos, Array<Producto>::class.java)
            return lista.toMutableList()
        }
        suspend fun getProducto(id:Int): Producto {
            val datos = WebServiceREST.get(url+"/productos/detalleMovil/"+id)
            return Gson().fromJson<Producto>(datos, Producto::class.java)
        }

        /*suspend fun agregarCarrito(usuario:Usuario, producto:Producto, cantidad:Int): MutableList<Carrito> {
            val datos = WebServiceREST.get(url+"/carrito/agregar/"+usuario.id+"/"+producto.id+"/"+cantidad)
            val lista = Gson().fromJson<Array<Producto>>(datos, Array<Producto>::class.java)
            return lista.toMutableList()
        }*/
    }
}
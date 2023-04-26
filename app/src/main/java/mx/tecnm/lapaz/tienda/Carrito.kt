package mx.tecnm.lapaz.tienda

import com.google.gson.Gson

class Carrito(
    var id:Int=0,
    var nombre:String="",
    var precio:Float=0f,
    var cantidad:Int=0,
    var envio:Float=0f,
    var detalle:String=""
){
    companion object {
        /*
        suspend fun show(): MutableList<Producto> {
            val datos = WebServiceREST.get(url+"/productos/show")
            val lista = Gson().fromJson<Array<Producto>>(datos, Array<Producto>::class.java)
            return lista.toMutableList()
        }
         */
        suspend fun show(token:String):MutableList<Carrito>
        {
            val postData = "token="+token
            val respuesta = WebServiceREST.post(url+"/carrito/showMovil", postData)
            val lista = Gson().fromJson<Array<Carrito>>(respuesta, Array<Carrito>::class.java)
            return lista.toMutableList()
        }
        suspend fun add(token:String, idProducto:Int, cantidad:Int):String
        {
            val postData = "token="+token+"&"+"producto="+idProducto+"&"+"cantidad="+cantidad
            val respuesta = WebServiceREST.post(url+"/carrito/agregarMovil", postData)
            //val lista = Gson().fromJson<Array<String>>(respuesta, Array<Carrito>::class.java)
            return "z"
        }
    }
}
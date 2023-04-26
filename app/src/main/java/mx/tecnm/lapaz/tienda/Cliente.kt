package mx.tecnm.lapaz.tienda

import com.google.gson.Gson

class Cliente(
    var id:Int = 0,
    var correo:String = "",
    var nombre:String = "",
    var apellido:String = "",
    var direccion:String = "",
    var token: String = ""

) {
    companion object{
        suspend fun login(correo:String, password:String):Cliente?
        {
            val postData = "correo="+correo+"&"+"password="+password
            val respuesta = WebServiceREST.post(url+"/clientes/loginMovil", postData)
            if (respuesta!="false") {
                return Gson().fromJson<Cliente>(respuesta, Cliente::class.java)
            }
            return null
        }
    }
}

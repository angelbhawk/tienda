package mx.tecnm.lapaz.tienda

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        val main = (activity as MainActivity)
        val id = main.cliente?.id

        if(id!=null)
        {
            findNavController().navigate(R.id.action_loginFragment_to_productosFragment)
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val etCorreo = view.findViewById<EditText>(R.id.etCorreo)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        btnLogin.setOnClickListener {
            val correo = etCorreo.text.toString()
            val password = etPassword.text.toString()
            val scope = CoroutineScope(Job())
            scope.launch {
                val cliente = Cliente.login(correo, password)
                val main = (activity as MainActivity)
                main.runOnUiThread {
                    if (cliente != null) {
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                        if (sharedPref!=null){
                            with (sharedPref.edit()) {
                                putString(getString(R.string.cliente), Gson().toJson(cliente))
                                    .apply()
                            }
                        }
                        main.cliente = cliente
                        //main.mostrarProductosFragment()
                        findNavController().navigate(R.id.action_loginFragment_to_productosFragment)
                    } else {
                        etPassword.setText("")
                        Toast.makeText(
                            context,
                            "Error correo o contraseña incorrectos",
                            Toast.LENGTH_LONG
                        );
                    }
                }
            }
        }
    }
}
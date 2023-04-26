package mx.tecnm.lapaz.tienda

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductoFragment : Fragment() {
    lateinit var producto : Producto
    lateinit var ivCamara: ImageView
    lateinit var ivPImagen : ImageView
    lateinit var root : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            producto = it.getSerializable("producto") as Producto
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_producto, container, false)
        val tvPNombre = root.findViewById<TextView>(R.id.tvPNombre)
        val tvPDetalle = root.findViewById<TextView>(R.id.tvPDetalle)
        val tvPrecio = root.findViewById<TextView>(R.id.tvPPrecio)
        val etCantidad = root.findViewById<EditText>(R.id.etCantidad)
        val btnAgregar = root.findViewById<Button>(R.id.btnAgregar)
        ivCamara = root.findViewById<ImageView>(R.id.ivCamara)
        ivPImagen = root.findViewById<ImageView>(R.id.ivPImagen)

        val scope4 = CoroutineScope(Job())
        scope4.launch {
            val lista = Producto.show()
            val fileName = producto.id.toString()+".jpg"
            val bitmap = WebServiceREST.getImage(url + "/storage/imagenes/" + fileName)

            (context as Activity).runOnUiThread{
                if(bitmap!=null)
                {
                    ivPImagen.setImageBitmap(bitmap)
                }
            }
        }

        tvPNombre.text = producto.nombre
        tvPDetalle.text = producto.detalle
        tvPrecio.text = (producto.precio + producto.envio).toString()

        btnAgregar.setOnClickListener {
            val main = (activity as MainActivity)
            var token = ""
            token = ""+main.cliente?.token

            val id = producto.id
            var cantidad = 0
            cantidad = 0+(etCantidad.text.toString()+"").toInt()

            val scope = CoroutineScope(Job())
            scope.launch {
                val resultado = Carrito.add(token, id, cantidad.toInt())
                val main = (activity as MainActivity)
                main.runOnUiThread {

                        Toast.makeText(
                            context,
                            resultado,
                            Toast.LENGTH_LONG
                        );
                }
            }
        }

        ivCamara.setOnClickListener {
            val scope = CoroutineScope(Job())
            scope.launch {
                requireActivity().runOnUiThread {
                    val main = requireActivity() as MainActivity
                    if (main.cliente?.id == 1)
                    {
                        dispatchTakePictureIntent()
                    }
                }
            }
        }
        return root
    }

    val RESULT_OK = 1;

    private var cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //if (result.resultCode == RESULT_OK) {
            val fileName = "tmp.jpg"
            val bitmap = result.data?.extras?.get("data")
            if (bitmap is Bitmap) {
                requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)
                    .use { fos ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, fos)
                    }
                val main = requireActivity() as MainActivity
                val scope = CoroutineScope(Job())
                scope.launch {
                    val respuesta = WebServiceREST.postFile(
                        url + "/producto/modificarImagen",
                        listOf(
                            Parametro("id", producto.id.toString()),
                            Parametro("token", main.cliente!!.token)
                        ), "imagen",  fileName,
                        requireContext().openFileInput(fileName)
                    )
                    if (respuesta == "ok") {
                        // cambios

                        val bitmap = BitmapFactory.decodeStream(
                            requireContext().openFileInput(fileName).readBytes().inputStream())
                        (context as Activity).runOnUiThread {
                            ivPImagen.setImageBitmap(bitmap)
                        }
                    }
                    else
                    {
                        Log.e("myTag", "aaaa"+respuesta);
                    }
                }
            }
            //ivCamara.setImageBitmap(imageBitmap)
            //}
        }

    fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            cameraActivityResultLauncher.launch(takePictureIntent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Producto) =
            ProductoFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}
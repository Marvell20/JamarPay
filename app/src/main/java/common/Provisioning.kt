package common
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.jamarpay.ProvisioningItem
import com.example.jamarpay.R
import com.google.android.material.button.MaterialButton
import com.example.jamarpay.ApiService
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class Provisioning: DialogFragment() {
    private var value: String? = null

    companion object {
        fun newInstance(value: String): Provisioning {
            val fragment = Provisioning()
            val args = Bundle()
            args.putString("value", value)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.aprovisionamiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelButton = view.findViewById<MaterialButton>(R.id.TermsAndConditionsCancelButton)

        // Configura el listener para el evento de clic del botón
        cancelButton.setOnClickListener {
            dismiss()
        }

        // Obtén el valor pasado desde la clase principal
        value = arguments?.getString("value")
        val ID = GlobalData.Identificacion
        Log.i("identificacion", ID.toString())

        //servicio aprovisionamiento


        runBlocking {
            launch(Dispatchers.IO) {
                // Crear una instancia de Retrofit
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://dev.appsjamar.com/credito/payoro/aprovisionamiento/") // Reemplaza con la URL base de tu servicio
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crear una instancia de ApiService utilizando Retrofit
                val apiService = retrofit.create(ApiService::class.java)

                // Crear el objeto ProvisioningItem
                val provisioningItem = ProvisioningItem("JA", ID, "E", getUUID())

                val gson = Gson()
                val json = gson.toJson(provisioningItem)

                val mediaType = "application/json".toMediaTypeOrNull()
                val requestBody = json.toRequestBody(mediaType)

                // Realizar la llamada al servicio
                val response = apiService.sendAprovisionamiento("nombre_de_la_empresa", requestBody)
                Log.i("respuesta", response.body().toString())
                // Verificar la respuesta
                /* if (response.isSuccessful) {
                    val provisioningResponse = response.body()
                    // Acceder a los datos de la respuesta
                    val cEmp = provisioningResponse?.c_emp
                    val nIde = provisioningResponse?.n_ide
                    Log.i("respuesta", provisioningItem.toString())
                } else {
                    // Ocurrió un error en la respuesta del servicio
                }*/
            }
        }

        // Configura la lógica y los eventos del fragmento de diálogo aquí, utilizando el valor si es necesario

    }

    private fun getUUID(): String {
        val uuid: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        return uuid
    }
}
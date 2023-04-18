package common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.jamarpay.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.NetworkInterface
import java.util.*


class DialogTermsAndConditions: DialogFragment() {

    data class Response(
        val success: Boolean,
        val data: Data,
        val msg: String
    )

    data class Data(
        val id: Int,
        val c_emp: String,
        val description: String,
        val version: String,
        val status: String
    )

    private var listener: DialogFragmentListener? = null

    interface DialogFragmentListener {
        fun onAcceptClicked()
        fun onCancelClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Verifica que la actividad actual implemente la interfaz
        if (context is DialogFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_terms_and_conditions, container, false)

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtBodyTerms = rootView.findViewById<TextView>(R.id.TermsAndConditionsBodyTextView)
        txtBodyTerms.movementMethod = ScrollingMovementMethod()

        val btnAccept = rootView.findViewById<MaterialButton>(R.id.TermsAndConditionsAcceptButton)
        val btnCancel = rootView.findViewById<MaterialButton>(R.id.TermsAndConditionsCancelButton)

        btnCancel.setOnClickListener {
            listener!!.onCancelClicked()
            guardarRespuestaTermsNConditions("N")
            dialog!!.dismiss()
        }

        btnAccept.setOnClickListener {
            listener!!.onAcceptClicked()
            guardarRespuestaTermsNConditions("A")
            dialog!!.dismiss()
        }

        val textView = rootView.findViewById<MaterialTextView>(R.id.TermsAndConditionsBodyTextView)
        val handler = Handler(Looper.getMainLooper())
        // Crear una instancia de OkHttpClient
        val client = OkHttpClient()

        // Crear una solicitud HTTP GET a la URL del servicio web
        val request = Request.Builder()
            .url("https://dev.appsjamar.com/credito/payoro/obtener_texto_tyc/JA")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                // Verificar que la respuesta sea exitosa (código de estado 200)
                if (response.code == 200) {
                    // Obtener la respuesta en forma de cadena de texto
                    val jsonResponse = response.body?.string()

                    // Parsear la respuesta a un objeto de la clase de modelo utilizando Gson
                    val gson = Gson()
                    val responseObject = gson.fromJson(jsonResponse, Response::class.java)

                    // Obtener el valor de la clave "description" del objeto responseObject
                    val description = responseObject.data.description

                    // Actualizar el TextView en el hilo principal de la interfaz de usuario
                    handler.post {
                        // Actualizar el TextView en el hilo principal de la interfaz de usuario
                        textView.text = description
                    }
                } else {
                    System.out.println("No hubo respuesta de la petición")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                System.out.println("Error")
            }
        })

        return rootView
    }

    fun guardarRespuestaTermsNConditions(respuesta: String) {

        fun createJsonBody(): JSONObject {
            val jsonBody = JSONObject()
            jsonBody.put("n_ide", "1234500")
            jsonBody.put("c_emp", "JA")
            jsonBody.put("ip", "127.0.0.2")
            jsonBody.put("mac", "122325")
            jsonBody.put("imei", "33445335")
            jsonBody.put("brand", "Samsung")
            jsonBody.put("model_brand", "Galaxy S9")
            jsonBody.put("android_version", "12")
            jsonBody.put("first_time", "S")
            jsonBody.put("answer", respuesta)
            jsonBody.put("status", "A")
            jsonBody.put("created_at", "2023/04/17")
            jsonBody.put("updated_at", "")

            return jsonBody
        }

        val client = OkHttpClient()
        val url = "https://dev.appsjamar.com/credito/payoro/guardar_respuesta_tyc/JA" // Cambiar por la URL de tu API
        val jsonBody = createJsonBody() // Crear el cuerpo de la solicitud en formato JSON

        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar el error de la petición
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                // Manejar la respuesta de la petición
                val jsonResponse = response.body?.string()
                val gson = Gson()
                val responseObject = gson.fromJson(jsonResponse, Response::class.java)

                if (responseObject.success) {
                    println("Respuesta del servidor: Ok")
                } else {
                    // Manejar el caso de una respuesta no exitosa
                    println("Respuesta no exitosa del servidor: ${response.code} ${response.message}")
                }
            }
        })
    }


    fun getIPAddress(): String? {
        try {
            val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress && addr.hostAddress.indexOf(':') < 0) {
                        return addr.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getMACAddress(): String? {
        try {
            val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                if (intf.name.equals("wlan0", ignoreCase = true)) {
                    val macBytes = intf.hardwareAddress
                    if (macBytes != null) {
                        val macStringBuilder = StringBuilder()
                        for (b in macBytes) {
                            macStringBuilder.append(String.format("%02X:", b))
                        }
                        if (macStringBuilder.isNotEmpty()) {
                            macStringBuilder.deleteCharAt(macStringBuilder.length - 1)
                        }
                        return macStringBuilder.toString()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getDeviceBrandAndModel(): String {
        val brand = Build.BRAND
        val model = Build.MODEL
        return "$brand $model"
    }

    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }

}


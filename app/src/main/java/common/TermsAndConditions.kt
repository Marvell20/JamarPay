package common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.jamarpay.MainActivity
import com.example.jamarpay.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*


class TermsAndConditions: DialogFragment() {
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

        val mainActivity = activity as MainActivity
        val cedula = mainActivity.getEditTextValue()

        var rootView: View = inflater.inflate(R.layout.dialog_terms_and_conditions, container, false)

        val roundedBackground = ContextCompat.getDrawable(requireContext(), R.drawable.border_radious)
        rootView.background = roundedBackground

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtBodyTerms = rootView.findViewById<TextView>(R.id.TermsAndConditionsBodyTextView)
        txtBodyTerms.movementMethod = ScrollingMovementMethod()

        val btnAccept = rootView.findViewById<MaterialButton>(R.id.TermsAndConditionsAcceptButton)
        val btnCancel = rootView.findViewById<MaterialButton>(R.id.TermsAndConditionsCancelButton)

        btnCancel.setOnClickListener {
            listener!!.onCancelClicked()
            guardarRespuestaTermsNConditions(cedula, "R", "N")
            dialog!!.dismiss()
        }

        btnAccept.setOnClickListener {
            listener!!.onAcceptClicked()



            guardarRespuestaTermsNConditions(cedula,"A", "A")
            dialog!!.dismiss()
        }

        val textView = rootView.findViewById<MaterialTextView>(R.id.TermsAndConditionsBodyTextView)
        val handler = Handler(Looper.getMainLooper())
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://dev.appsjamar.com/credito/payoro/obtener_texto_tyc/JA")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {

                if (response.code == 200) {

                    val jsonResponse = response.body?.string()

                    val gson = Gson()
                    val responseObject = gson.fromJson(jsonResponse, Response::class.java)

                    val description = responseObject.data.description

                    // Actualizar el TextView en el hilo principal de la interfaz de usuario
                    handler.post {
                        // Actualizar el TextView en el hilo principal de la interfaz de usuario
                        //textView.text = description
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

    fun guardarRespuestaTermsNConditions(cedula: String, respuesta: String, first_time: String) {

        fun createJsonBody(): JsonObject {

            val gson = GsonBuilder().setDateFormat("yyyy/MM/dd").create()
            val jsonBody = JsonObject()
            jsonBody.addProperty("n_ide", cedula)
            jsonBody.addProperty("c_emp", "JA")
            jsonBody.addProperty("ip", getIPAddress())
            jsonBody.addProperty("mac", "mac")
            jsonBody.addProperty("uuid", getUUID())
            jsonBody.addProperty("brand", getDeviceBrand())
            jsonBody.addProperty("model_brand", getDeviceModel())
            jsonBody.addProperty("android_version", getAndroidVersion())
            jsonBody.addProperty("first_time", first_time)
            jsonBody.addProperty("answer", respuesta)
            jsonBody.addProperty("status", "A")
            jsonBody.addProperty("created_at", getCurrentDate())
            jsonBody.addProperty("updated_at", "")

            println("JSON: " + jsonBody)

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
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                val jsonResponse = response.body?.string()
                val gson = Gson()
                val responseObject = gson.fromJson(jsonResponse, Response::class.java)

                if (responseObject.success) {
                    println("Respuesta del servidor: Ok")
                } else {
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

    private fun getUUID(): String {
        val uuid: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        return uuid
    }

    fun getDeviceBrand(): String {
        val brand = Build.BRAND
        return "$brand"
    }

    fun getDeviceModel(): String {
        val model = Build.MODEL
        return "$model"
    }

    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale("es", "ES"))
        sdf.timeZone = TimeZone.getTimeZone("UTC-5")
        val currentdate = sdf.format(Date())
        Log.d("fechaactual", currentdate)

        return currentdate
    }
}

private fun MainActivity.getEditTextValue(): String {
    val editText = findViewById<EditText>(R.id.editTextTextPersonName)
    return editText.text.toString()
}


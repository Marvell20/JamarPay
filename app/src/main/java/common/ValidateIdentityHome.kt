package common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.jamarpay.ActivityBecomeSdk
import com.example.jamarpay.ApiService
import com.example.jamarpay.R
import com.example.jamarpay.ResponseAddBecome
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ValidateIdentityHome : AppCompatActivity() {

    private val validateIdentity = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.validate_identity_home)

        val button = findViewById<Button>(R.id.ValidateIdentity)

        button.setOnClickListener {
            validateIdentity.launch {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://dev.appsjamar.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val getNextProcess = retrofit.create(ApiService::class.java)
                val nextProcess = getNextProcess.getNextProcess(GlobalData.Identificacion,"JA")
                Log.i("NextProcess",nextProcess.body().toString())

                if (nextProcess.isSuccessful) {
                    val provisioning = nextProcess.body()?.provisionamiento
                    val validenti = nextProcess.body()?.validation_identity
                    val intentos = nextProcess.body()?.attempts_vi

                    if (validenti == true && intentos == true) {
                        val intent = Intent(this@ValidateIdentityHome, ActivityBecomeSdk::class.java)
                        startActivity(intent)
                        finish()
                    } else if (validenti == true && intentos == true) {
                        //TO-DO poner pantalla de josue
                        Log.i("Become", "Mostrar pantalla de josue")

                    } else if (validenti == true && intentos == false) {
                        //TO-DO poner pantalla de breiner
                        Log.i("Become", "Mostrar pantalla de josue")
                    } else if (provisioning == true) {
                        val intent = Intent(this@ValidateIdentityHome, ConfirmedIdentity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent =
                            Intent(this@ValidateIdentityHome, DeviceAlreadyProvisioned::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    fun saveValidateIdentity(n_ide: String,
                             c_emp: String,
                             uuid: String,
                             status: String,
                             result_status: String
    ) {

        fun createJsonBody(): JsonObject {
            val jsonBody = JsonObject();
            jsonBody.addProperty("n_ide", n_ide)
            jsonBody.addProperty("c_emp", c_emp)
            jsonBody.addProperty("status", "G")
            jsonBody.addProperty("result_status", "")
            return jsonBody;
        }

        val client = OkHttpClient()
        val url = "https://dev.appsjamar.com/credito/payoro/insert-validate_identity/JA" // Cambiar por la URL de tu API
        val jsonBody = createJsonBody() // Crear el cuerpo de la solicitud en formato JSON

        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object: okhttp3.Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body?.string()
                val gson = Gson()
                val responseObject = gson.fromJson<ResponseAddBecome>(jsonResponse, Response::class.java)

                if( responseObject.success) {
                    Log.i("addBecome", "se creo el registro exitosamente")
                } else {
                    Log.i("addBecome", "no se creo el registro")
                }
            }
        })

    }
}
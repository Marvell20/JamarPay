package common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.jamarpay.ActivityBecomeSdk
import com.example.jamarpay.ApiService
import com.example.jamarpay.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
}
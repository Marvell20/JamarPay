package common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.jamarpay.ActivityBecomeSdk
import com.example.jamarpay.ApiService
import com.example.jamarpay.R
import com.example.jamarpay.ResponseAddBecome
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import android.provider.Settings.Secure
import com.example.jamarpay.RequestAddBecome
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.runBlocking
import retrofit2.Call

class ValidateIdentityHome : AppCompatActivity() {

    private val validateIdentity = CoroutineScope(Dispatchers.IO)
    private val validateBecomeScope = CoroutineScope(Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.validate_identity_home)

        val button = findViewById<Button>(R.id.ValidateIdentity)

        button.setOnClickListener {

            saveValidateIdentity(
                n_ide = GlobalData.Identificacion,
                c_emp = "JA",
                uuid = getUUID(),
                status = "G",
                result_status = ""
            )

        }
    }

    fun saveValidateIdentity(n_ide: String,
                                     c_emp: String,
                                     uuid: String,
                                     status: String,
                                     result_status: String
    ) {

        validateBecomeScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dev.appsjamar.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiservice = retrofit.create(ApiService::class.java)

            val become = RequestAddBecome(
                n_ide,
                c_emp,
                getUUID(),
                "",
                "G",
                ""
            )

            val callApiService = apiservice.sendValidateIdentity("JA",become)
            GlobalData.UserId = callApiService.body()?.userid ?: 0

            runBlocking {
                validateBecomeScope.coroutineContext.cancelChildren()
                GlobalData.UserId = callApiService.body()?.userid ?: 0
                //Call activity of sdk become
                Log.i("userid",GlobalData.UserId.toString())
                val intent = Intent(this@ValidateIdentityHome, ActivityBecomeSdk::class.java)
                intent.putExtra("userid", GlobalData.UserId.toString())
                startActivity(intent)
                finish()
            }
        }

    }

    fun getUUID(): String {
        val androidId = Secure.getString(
            applicationContext.contentResolver,
            Secure.ANDROID_ID
        )

        return androidId.toString()
    }
}
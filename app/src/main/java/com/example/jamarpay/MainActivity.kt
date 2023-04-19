package com.example.jamarpay

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import common.DialogTermsAndConditions
import common.DialogTermsAndConditions.DialogFragmentListener
import common.Identity
import common.LoadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), DialogFragmentListener {

    private lateinit var checkBox: CheckBox
    private lateinit var btnNext: Button
    private lateinit var checkBoxText: TextView

    private val validateGoldClientScope = CoroutineScope(Dispatchers.IO)

    override fun onAcceptClicked() {
        checkBox.isChecked = true
    }

    override fun onCancelClicked() {
        if (checkBox.isChecked) {
            checkBox.isChecked = false
        };
    }

    private val secondScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handler = Handler(Looper.getMainLooper())

        setContentView(R.layout.splash)

        handler.postDelayed({ // inflar el segundo archivo de diseño XML en la vista principal
            setContentView(R.layout.login)

            checkBox = findViewById(R.id.checkBox)
            checkBoxText = findViewById(R.id.checkBoxText)
            btnNext = findViewById(R.id.button)

            checkBox.isClickable = false

            btnNext.setOnClickListener {

                if (checkBox.isChecked) {
                    val mainActivity = MainActivity()
                    val fragment = LoadingView()
                    val identidadConfirmada = Identity()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.LoginLayout, fragment)
                        .commit()

                    validateGoldClientScope.launch {
                        val retrofit = Retrofit.Builder()
                            .baseUrl("https://dev.appsjamar.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                        val gettingGoldClient = retrofit.create(ApiService::class.java)
                        val goldClient = gettingGoldClient.getGoldClientValidation("JA", findViewById<EditText>(R.id.editTextTextPersonName).text.toString())
                        Log.i("GoldClientValidation",goldClient.body().toString())

                        if (goldClient.body()?.data?.segmento == "ORO" || goldClient.body()?.data?.segmento == "PORO") {

                            supportFragmentManager.beginTransaction()
                                .remove(fragment)
                                .commit()

                            supportFragmentManager.beginTransaction()
                                .replace(R.id.LoginLayout, identidadConfirmada)
                                .commit()

                        } else {
                            println("Cliente no es oro")
                        }
                    }

                } else {
                    Toast.makeText(this, "Por favor, acepta los términos y condiciones.", Toast.LENGTH_SHORT).show()
                }
            }

            checkBoxText.setOnClickListener {
                val dialogFragment  = DialogTermsAndConditions()
                dialogFragment.show(supportFragmentManager, "customDialog")
            }

            //BREINER

            secondScope.launch {

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://16oardvn23.execute-api.us-east-1.amazonaws.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val gettingDocumentType = retrofit.create(ApiService::class.java)
                val documentType = gettingDocumentType.getDocumentType("JA")
                Log.i("DocumentType", documentType.body().toString())
                val documentTypeList: List<DocumentTypeItem> = documentType.body()?.data ?: emptyList()
                val labels = documentTypeList.map { it.label }
                val mySpinner = findViewById<Spinner>(R.id.my_spinner)
                Log.i("Label", labels.toString())
                handler.post {
                    val adapter =
                        ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, labels)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    mySpinner.adapter = adapter
                }

                val editText = findViewById<EditText>(R.id.editTextTextPersonName)
                val button = findViewById<MaterialButton>(R.id.button)
                button.isEnabled = false

                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        // No se usa
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        button.isEnabled = !s.isNullOrEmpty()
                    }

                    override fun afterTextChanged(s: Editable?) {
                        // No se usa
                    }
                })

                fun getEditTextValue(): String {
                    return editText.text.toString()
                }
            }

        }, 5000) // tiempo de retraso en milisegundos (5 segundos)
    }
}

    package com.example.jamarpay

    import android.content.Intent
    import android.graphics.Color
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.text.Editable
    import android.text.InputType
    import android.text.TextWatcher
    import android.util.Log
    import android.view.View
    import android.widget.*
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.content.ContextCompat
    import com.google.android.material.button.MaterialButton
    import common.*
    import common.TermsAndConditions.DialogFragmentListener
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import common.GlobalData
    import okhttp3.Dispatcher

    class MainActivity : AppCompatActivity(), DialogFragmentListener {

        private lateinit var checkBox: CheckBox
        private lateinit var btnNext: Button
        private lateinit var checkBoxText: TextView
        private lateinit var spinner: Spinner
        private var isNumeric = false

        private val validateGoldClientScope = CoroutineScope(Dispatchers.IO)
        private val secondScope = CoroutineScope(Dispatchers.IO)
        private val validateWorkflowScope = CoroutineScope(Dispatchers.IO)

        override fun onAcceptClicked() {
            checkBox.isChecked = true
        }

        override fun onCancelClicked() {
            if (checkBox.isChecked) {
                checkBox.isChecked = false
            };
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.identidad_confirmada)

            val handler = Handler(Looper.getMainLooper())

            setContentView(R.layout.splash)

            handler.postDelayed({ // inflar el segundo archivo de diseño XML en la vista principal
                setContentView(R.layout.login)
                //val intent = Intent(this, CustomerVerificationScreen::class.java)
                //startActivity(intent)
                //finish()

                checkBox = findViewById(R.id.checkBox)
                checkBoxText = findViewById(R.id.checkBoxText)
                btnNext = findViewById(R.id.button)

                checkBox.isClickable = false

                btnNext.setOnClickListener {

                    val editText = findViewById<EditText>(R.id.editTextTextPersonName)
                    GlobalData.Identificacion = editText.text.toString()

                    if (checkBox.isChecked) {
                        val intent = Intent(this@MainActivity, LoadingView::class.java)
                        startActivity(intent)
                        finish()


                        validateGoldClientScope.launch {
                            val retrofit = Retrofit.Builder()
                                .baseUrl("https://dev.appsjamar.com")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

                            val gettingGoldClient = retrofit.create(ApiService::class.java)
                            val goldClient = gettingGoldClient.getGoldClientValidation("JA", findViewById<EditText>(R.id.editTextTextPersonName).text.toString())
                            Log.i("GoldClientValidation",goldClient.body().toString())

                            if (goldClient.isSuccessful) {
                                val response = goldClient.body()
                                val segmento = response?.data?.segmento
                                Log.i("el segmento es: ", segmento.toString())

                                if (segmento == "ORO" || segmento == "PORO" || segmento == "EJEMPLAR" ||
                                    segmento == "PEJEMPLAR" || segmento == "RENTABLE" || segmento == "PRENTABLE"
                                    || segmento == "CLIENTE NUEVO")

                                {

                                    validateWorkflowJamarpay("JA",GlobalData.Identificacion)


                                } else {
                                    val intent = Intent(this@MainActivity, UnconfirmedIdentity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                Log.e("GoldClientValidation", "Error: ${goldClient.code()}")
                            }
                        }

                    } else {
                        Toast.makeText(this, "Por favor, acepta los términos y condiciones.", Toast.LENGTH_SHORT).show()
                    }
                }

                checkBoxText.setOnClickListener {
                    val dialogFragment  = TermsAndConditions()
                    dialogFragment.show(supportFragmentManager, "customDialog")
                }

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
                    val color = ContextCompat.getColor(this@MainActivity, R.color.gray_btn)
                    val colorText = ContextCompat.getColor(this@MainActivity, R.color.prompt_button_text)
                    button.setBackgroundColor(color)
                    button.setTextColor(colorText)

                    val spinner = findViewById<Spinner>(R.id.my_spinner)

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedLabel = labels[position]

                            if (selectedLabel == "CEDULA DE CIUDADANIA") {
                                editText.inputType = InputType.TYPE_CLASS_NUMBER
                            } else {
                                editText.inputType = InputType.TYPE_CLASS_TEXT
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // No se usa
                        }
                    }

                    editText.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            // No se usa
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            if (!s.isNullOrEmpty() && s.length >= 4) {
                                button.isEnabled = true
                                button.setBackgroundColor(Color.BLACK)
                                button.setTextColor(Color.WHITE)
                            } else {
                                button.isEnabled = false
                                //button.setBackgroundColor(color)
                                //button.setTextColor(colorText)
                            }

                        }

                        override fun afterTextChanged(s: Editable?) {
                            // No se usa
                        }
                    })

                    fun getEditTextValue(): String {
                        return editText.text.toString()
                    }
                }

            }, 3000) // tiempo de retraso en milisegundos (5 segundos)
        }

        fun validateWorkflowJamarpay(company: String, n_ide: String){
            validateWorkflowScope.launch {
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

                        val intent = Intent(this@MainActivity, ValidateIdentityHome::class.java)
                        startActivity(intent)
                        finish()

                    } else if (validenti == true && intentos == false) {
                        //TO-DO poner pantalla de breiner
                        Log.i("Become", "Mostrar pantalla de breiner")
                        val intent = Intent(this@MainActivity, FailedAttempts::class.java)
                        startActivity(intent)
                        finish()
                    } else if (provisioning == true) {
                        val intent = Intent(this@MainActivity, ConfirmedIdentity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@MainActivity, DeviceAlreadyProvisioned::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

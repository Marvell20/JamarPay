package common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.jamarpay.R
import com.google.android.material.button.MaterialButton
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class DialogTermsAndConditions: DialogFragment() {

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
            dialog!!.dismiss()
        }

        btnAccept.setOnClickListener {
            listener!!.onAcceptClicked()
            dialog!!.dismiss()
        }

        return rootView
    }

    fun obtenerTextoDesdeServicio(url: String): String {
        var texto: String = ""

        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 10000 // Configura el tiempo máximo de espera en milisegundos
            connection.readTimeout = 10000 // Configura el tiempo máximo de lectura en milisegundos

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))

                bufferedReader.useLines { lines ->
                    texto = lines.joinToString(separator = "\n") // Concatena las líneas del texto en un solo String
                }

                bufferedReader.close()
                inputStream.close()
            }

            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return texto
    }
}


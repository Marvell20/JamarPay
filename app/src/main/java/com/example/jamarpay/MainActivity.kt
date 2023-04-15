package com.example.jamarpay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import common.DialogTermsAndConditions
import common.DialogTermsAndConditions.DialogFragmentListener

class MainActivity : AppCompatActivity(), DialogFragmentListener {

    override fun onAcceptClicked() {
        checkBox.isChecked = true
    }

    override fun onCancelClicked() {
        if (checkBox.isChecked) {
            checkBox.isChecked = false
        };
    }

    private lateinit var checkBox: CheckBox
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkBox = findViewById(R.id.checkBox)
        btnNext = findViewById(R.id.button)

        btnNext.setOnClickListener {

            if (checkBox.isChecked) {
                //Toast.makeText(this, "¡CheckBox marcado! Puedes avanzar a la siguiente pantalla.", Toast.LENGTH_SHORT).show()
                setContentView(R.layout.loading_view)
            } else {
                Toast.makeText(this, "Por favor, acepta los términos y condiciones.", Toast.LENGTH_SHORT).show()
            }
        }

        checkBox.setOnClickListener {
            val dialogFragment  = DialogTermsAndConditions()
            dialogFragment.show(supportFragmentManager, "customDialog")
        }
    }

    private fun mostrarModalTerminosCondiciones() {
        val dialogFragment = DialogTermsAndConditions()
        dialogFragment.show(supportFragmentManager, "termsAndConditionsDialog")
    }

    private fun ocultarModalTerminosCondiciones() {
        // Aquí puedes implementar la lógica para ocultar el modal con los términos y condiciones
    }
}
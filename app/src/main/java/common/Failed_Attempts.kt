package common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.jamarpay.R

class Failed_Attempts: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intentos_agotados)

        val loginReverseButton = findViewById<Button>(R.id.LoginReverse)
        loginReverseButton.setOnClickListener {
            // Iniciar la nueva actividad que muestra el XML de login
            setContentView(R.layout.login)
        }
    }
}
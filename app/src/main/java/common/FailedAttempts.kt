package common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.jamarpay.MainActivity
import com.example.jamarpay.R

class FailedAttempts: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intentos_agotados)

        val loginReverseButton = findViewById<Button>(R.id.LoginReverse)
        loginReverseButton.setOnClickListener {
            // Iniciar la nueva actividad que muestra el XML de login
            val intent = Intent(this@FailedAttempts,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
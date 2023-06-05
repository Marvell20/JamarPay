package common

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.jamarpay.MainActivity
import com.example.jamarpay.R

class Become_Unconfirmed: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.become_no_confirmado)

        val newAttemptsVi = findViewById<Button>(R.id.IntentarNuevamente)
        newAttemptsVi.setOnClickListener {
            val intent = Intent(this@Become_Unconfirmed, ValidateIdentityHome::class.java)
            startActivity(intent)
            finish()
        }
    }
}

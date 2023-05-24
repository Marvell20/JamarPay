package common

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.jamarpay.R

class ConfirmedIdentity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.identidad_confirmada)

        val btnAprov = findViewById<Button>(R.id.RegistrarAprov)

        btnAprov.setOnClickListener {
            val valueFromMain = "Valor desde la clase principal"
            val dialogFragment = Provisioning.newInstance(valueFromMain)
            dialogFragment.show(supportFragmentManager, "Aprovisionamiento")
        }

    }
}
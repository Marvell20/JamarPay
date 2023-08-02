package common

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.jamarpay.BottomNavigationViewJamarPay
import com.example.jamarpay.R

class DeviceProvisioning: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dispositivo_aprovisionado)

        val delayMillis: Long = 3000

        val handler = Handler(Looper.getMainLooper())

        val runnable = Runnable{
            val intent = Intent(this@DeviceProvisioning, BottomNavigationViewJamarPay::class.java)
            startActivity(intent)
            finish()
        }

        handler.postDelayed(runnable,delayMillis)

    }
}

package common
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.jamarpay.R

class Provisioning: DialogFragment() {
    private var value: String? = null

    companion object {
        fun newInstance(value: String): Provisioning {
            val fragment = Provisioning()
            val args = Bundle()
            args.putString("value", value)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.aprovisionamiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtén el valor pasado desde la clase principal
        value = arguments?.getString("value")

        // Configura la lógica y los eventos del fragmento de diálogo aquí, utilizando el valor si es necesario
    }
}
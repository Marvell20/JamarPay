package common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jamarpay.R

class LoadingView: Fragment() {
    //lateinit var progressIndicator: CircularProgressIndicator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.loading_view, container, false)

        //progressIndicator = rootView.findViewById(R.id.circularProgressIndicator)

        return rootView
    }


}
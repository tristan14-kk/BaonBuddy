package com.mobdev.baonbuddy.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobdev.baonbuddy.R

class GetStartedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_get_started, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getStartedButton = view.findViewById<Button>(R.id.getStartedButton)

        getStartedButton.setOnClickListener {
            findNavController().navigate(R.id.nameInputFragment)
        }
    }
}

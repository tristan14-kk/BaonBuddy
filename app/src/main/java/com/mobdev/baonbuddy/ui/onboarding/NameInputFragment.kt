package com.mobdev.baonbuddy.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobdev.baonbuddy.R

class NameInputFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_name_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput = view.findViewById<EditText>(R.id.nameInput)
        val continueButton = view.findViewById<Button>(R.id.continueButton)

        continueButton.setOnClickListener {
            val name = nameInput.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                putString("userName", name)
            }
            findNavController().navigate(R.id.avatarSelectionFragment, bundle)
        }
    }
}
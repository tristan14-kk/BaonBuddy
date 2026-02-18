package com.mobdev.baonbuddy.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetPinFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val pinInput = view.findViewById<EditText>(R.id.pinInput)
        val confirmPinInput = view.findViewById<EditText>(R.id.confirmPinInput)
        val savePinButton = view.findViewById<Button>(R.id.savePinButton)

        backButton.setOnClickListener { findNavController().navigateUp() }

        savePinButton.setOnClickListener {
            val pin = pinInput.text.toString()
            val confirmPin = confirmPinInput.text.toString()

            if (pin.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a PIN", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pin.length < 4) {
                Toast.makeText(requireContext(), "PIN must be at least 4 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pin != confirmPin) {
                Toast.makeText(requireContext(), "PINs do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repository = (requireActivity().application as BaonBuddyApplication).repository

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                repository.updatePin(pin, true)

                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "PIN saved!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }
}
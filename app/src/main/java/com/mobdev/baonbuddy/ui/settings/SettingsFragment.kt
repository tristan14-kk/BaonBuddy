package com.mobdev.baonbuddy.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val editNameButton = view.findViewById<LinearLayout>(R.id.editNameButton)
        val changeAvatarButton = view.findViewById<LinearLayout>(R.id.changeAvatarButton)
        val setPinButton = view.findViewById<LinearLayout>(R.id.setPinButton)
        val helpButton = view.findViewById<LinearLayout>(R.id.helpButton)
        val privacyButton = view.findViewById<LinearLayout>(R.id.privacyButton)
        val deleteDataButton = view.findViewById<LinearLayout>(R.id.deleteDataButton)

        backButton.setOnClickListener { findNavController().navigateUp() }

        editNameButton?.setOnClickListener { showEditNameDialog() }

        changeAvatarButton?.setOnClickListener {
            findNavController().navigate(R.id.changeAvatarFragment)
        }

        setPinButton?.setOnClickListener {
            findNavController().navigate(R.id.setPinFragment)
        }

        helpButton?.setOnClickListener {
            findNavController().navigate(R.id.helpSupportFragment)
        }

        privacyButton?.setOnClickListener {
            findNavController().navigate(R.id.privacyPolicyFragment)
        }

        deleteDataButton?.setOnClickListener { showDeleteConfirmationDialog() }
    }

    private fun showEditNameDialog() {
        val editText = EditText(requireContext())
        editText.hint = "Enter new name"

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Name")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val repository = (requireActivity().application as BaonBuddyApplication).repository
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        repository.updateUserName(newName)
                        launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Name updated!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All Data")
            .setMessage("Are you sure you want to delete all your data? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                val repository = (requireActivity().application as BaonBuddyApplication).repository

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    repository.deleteAllData()

                    val sharedPref = requireActivity().getSharedPreferences("BaonBuddyPrefs", Context.MODE_PRIVATE)
                    sharedPref.edit().clear().apply()

                    launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "All data deleted!", Toast.LENGTH_SHORT).show()
                        requireActivity().finish()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
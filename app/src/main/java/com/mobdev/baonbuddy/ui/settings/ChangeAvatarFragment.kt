package com.mobdev.baonbuddy.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.adapters.AvatarAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeAvatarFragment : Fragment() {

    private var selectedAvatarId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_avatar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val avatarGrid = view.findViewById<RecyclerView>(R.id.avatarGrid)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        val avatars = listOf(
            R.drawable.avatar_cat,
            R.drawable.avatar_chicken,
            R.drawable.avatar_elephant,
            R.drawable.avatar_koala,
            R.drawable.avatar_octopus,
            R.drawable.avatar_penguin,
            R.drawable.avatar_pig,
            R.drawable.avatar_snail,
            R.drawable.avatar_turtle
        )

        backButton.setOnClickListener { findNavController().navigateUp() }

        avatarGrid.layoutManager = GridLayoutManager(requireContext(), 3)
        avatarGrid.adapter = AvatarAdapter(avatars) { avatarId ->
            selectedAvatarId = avatarId
        }

        saveButton.setOnClickListener {
            if (selectedAvatarId == -1) {
                Toast.makeText(requireContext(), "Please select an avatar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repository = (requireActivity().application as BaonBuddyApplication).repository

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                repository.updateUserAvatar(selectedAvatarId)

                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Avatar updated!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }
}
package com.mobdev.baonbuddy.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.data.database.entity.UserEntity
import com.mobdev.baonbuddy.ui.adapters.AvatarAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvatarSelectionFragment : Fragment() {

    private var selectedAvatarId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_avatar_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val avatarGrid = view.findViewById<RecyclerView>(R.id.avatarGrid)
        val letsGoButton = view.findViewById<Button>(R.id.letsGoButton)

        val userName = arguments?.getString("userName") ?: "Friend"

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
        avatarGrid.layoutManager = GridLayoutManager(requireContext(), 3)
        avatarGrid.adapter = AvatarAdapter(avatars) { avatarId ->
            selectedAvatarId = avatarId
        }

        letsGoButton.setOnClickListener {
            if (selectedAvatarId == -1) {
                Toast.makeText(requireContext(), "Please select an avatar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repository = (requireActivity().application as BaonBuddyApplication).repository

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val user = UserEntity(
                    id = 1,
                    name = userName,
                    avatarResId = selectedAvatarId,
                    balance = 0.0,
                    joinDate = System.currentTimeMillis()
                )
                repository.createOrUpdateUser(user)

                val sharedPref = requireActivity().getSharedPreferences("BaonBuddyPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putBoolean("onboarding_complete", true).apply()

                launch(Dispatchers.Main) {
                    findNavController().navigate(R.id.mainFragment)
                }
            }
        }
    }
}
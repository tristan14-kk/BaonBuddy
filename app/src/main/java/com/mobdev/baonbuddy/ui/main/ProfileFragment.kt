package com.mobdev.baonbuddy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.viewmodel.ProfileViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        val userAvatar = view.findViewById<ImageView>(R.id.userAvatar)
        val userName = view.findViewById<TextView>(R.id.userName)
        val memberSince = view.findViewById<TextView>(R.id.memberSince)
        val totalSaved = view.findViewById<TextView>(R.id.totalSaved)
        val goalsCompleted = view.findViewById<TextView>(R.id.goalsCompleted)
        val settingsButton = view.findViewById<View>(R.id.settingsButton)

        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 0

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                userName.text = it.name
                userAvatar.setImageResource(it.avatarResId)
                totalSaved.text = "₱${formatter.format(it.balance)}"

                val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                memberSince.text = "Member since ${dateFormat.format(Date(it.joinDate))}"
            }
        }

        viewModel.completedGoals.observe(viewLifecycleOwner) { count ->
            goalsCompleted.text = "${count ?: 0}"
        }

        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
    }
}
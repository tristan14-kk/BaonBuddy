package com.mobdev.baonbuddy.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mobdev.baonbuddy.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val sharedPref = getSharedPreferences("BaonBuddyPrefs", Context.MODE_PRIVATE)
        val onboardingComplete = sharedPref.getBoolean("onboarding_complete", false)

        if (onboardingComplete) {
            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
            navGraph.setStartDestination(R.id.mainFragment)
            navController.graph = navGraph
        }
    }
}
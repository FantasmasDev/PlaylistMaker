package com.example.playlistmaker.presentation.ui.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.databinding.ActivityRootBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private val vm by viewModel<RootViewModel>()

    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.readTheme()
        vm.getViewState().observe(this) {
            (applicationContext as App).switcherTheme(it.currentThemeState.themeState)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavView = binding.bottomNavigationView

        bottomNavView.setupWithNavController(navController)
    }
}
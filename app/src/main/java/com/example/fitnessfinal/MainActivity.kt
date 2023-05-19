package com.example.fitnessfinal


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitnessfinal.databinding.ActivityMainBinding
import com.example.fitnessfinal.db.FitnessDataBase
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: MainViewModel
    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var loadingJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        sharedViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val dao = FitnessDataBase.getInstance(this).userDao()
        val factory = FitnessViewModelFactory(dao)
        fitnessViewModel = ViewModelProvider(this, factory)[FitnessViewModel::class.java]
        binding.viewModel = sharedViewModel
        binding.lifecycleOwner = this

        val view: View = binding.root
        setContentView(view)

        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menu_item_calories, R.id.menu_item_food, R.id.menu_item_settings)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)


    }
    override fun onDestroy() {
        super.onDestroy()
        if (loadingJob.isActive) {
            loadingJob.cancel()
        }
    }



}
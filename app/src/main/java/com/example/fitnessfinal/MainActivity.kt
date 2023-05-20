package com.example.fitnessfinal


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.fitnessfinal.databinding.ActivityMainBinding
import com.example.fitnessfinal.db.FitnessDataBase
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: MainViewModel
    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var loadingJob: Job
    lateinit var currentDataManager: CurrentDataManager

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
        setSupportActionBar(binding.toolbar)
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val appBarConfiguration = AppBarConfiguration(
            navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.toolbar.setNavigationOnClickListener{
            navController.navigateUp()
        }
        currentDataManager = CurrentDataManager(this)
        observeDataStore()

    }
    private fun observeDataStore(){
        currentDataManager.currentCalsFlow.asLiveData().observe(this) {
            sharedViewModel.currentCals.value = it.toInt().toString()
        }
        currentDataManager.currentProteinsFlow.asLiveData().observe(this) {
            sharedViewModel.currentProteins.value = it.toInt().toString()

        }
        currentDataManager.currentFatsFlow.asLiveData().observe(this) {
            sharedViewModel.currentFats.value = it.toInt().toString()
        }
        currentDataManager.currentCarbsFlow.asLiveData().observe(this) {
            sharedViewModel.currentCarbs.value = it.toInt().toString()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (loadingJob.isActive) {
            loadingJob.cancel()
        }
    }


}
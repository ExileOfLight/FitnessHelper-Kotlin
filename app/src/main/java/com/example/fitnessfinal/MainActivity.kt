package com.example.fitnessfinal


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.fitnessfinal.databinding.ActivityMainBinding
import com.example.fitnessfinal.db.FitnessDataBase
import com.example.fitnessfinal.db.FitnessRepository
import com.example.fitnessfinal.db.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: MainViewModel
    private lateinit var fitnessViewModel: FitnessViewModel
    //private lateinit var loadingJob: Job
    lateinit var currentDataManager: CurrentDataManager

    var curDate = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        sharedViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val dao = FitnessDataBase.getInstance(this).fitnessDao()
        val factory = FitnessViewModelFactory(FitnessRepository(dao))
        fitnessViewModel = ViewModelProvider(this, factory)[FitnessViewModel::class.java]
        binding.viewModel = sharedViewModel
        binding.lifecycleOwner = this

        val view: View = binding.root
        setContentView(view)


        currentDataManager = CurrentDataManager(this)


        setSupportActionBar(binding.toolbar)
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment).navController
        val appBarConfiguration = AppBarConfiguration(
            navController.graph
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }


        observeDataStore()
        sharedViewModel.loadDataFromDB(this@MainActivity, fitnessViewModel)




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
//    override fun onDestroy() {
//        super.onDestroy()
//        if (loadingJob.isActive) {
//            loadingJob.cancel()
//        }
//    }
    override fun onResume() {
        super.onResume()
        object : DateChangedBroadcastReceiver() {
            override fun onDateChanged(previousDate: Calendar, newDate: Calendar) {
                Log.d("AppLog", "MainActivity: ${DateChangedBroadcastReceiver.toString(previousDate)} -> ${DateChangedBroadcastReceiver.toString(newDate)}")
                curDate = newDate.clone() as Calendar
                //handle date change
                val everyMeal = fitnessViewModel.meals.value
                everyMeal?.forEach{ meal ->
                    val id = meal.id
                    val name = meal.foodName
                    val cals = meal.cals
                    val proteins = meal.proteins
                    val fats = meal.fats
                    val carbs = meal.carbs
                    lifecycleScope.launch {
                        fitnessViewModel.upsertMeal(Meal(id,name,cals,proteins, fats,carbs,0.0))
                    }

                }
            }
        }.registerOnResume(this, curDate)
    }
}
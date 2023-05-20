package com.example.fitnessfinal.fragmentsMain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fitnessfinal.FitnessViewModel
import com.example.fitnessfinal.FitnessViewModelFactory
import com.example.fitnessfinal.MainViewModel
import com.example.fitnessfinal.R
import com.example.fitnessfinal.databinding.FragmentCaloriesBinding
import com.example.fitnessfinal.db.FitnessDataBase
import com.example.fitnessfinal.db.FitnessRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
class CaloriesFragment : Fragment() {

    private lateinit var binding: FragmentCaloriesBinding
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var updateJob: Job
    private var dbNotLoaded = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calories, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = sharedViewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dao = FitnessDataBase.getInstance(requireContext()).fitnessDao()
        val factory = FitnessViewModelFactory(FitnessRepository(dao))
        fitnessViewModel = ViewModelProvider(this, factory)[FitnessViewModel::class.java]

    }

    override fun onResume() {
        super.onResume()
        if (dbNotLoaded) {
            //sharedViewModel.loadDataFromDB(viewLifecycleOwner, userViewModel)
            updateJob = CoroutineScope(Dispatchers.Main).launch {
                delay(100)
                sharedViewModel.updateMacros()
                dbNotLoaded = false
            }

        } else sharedViewModel.updateMacros()
        val sharedPrefs = requireActivity().getSharedPreferences("my_prefs", AppCompatActivity.MODE_PRIVATE)
        var isFirstRun = sharedPrefs.getBoolean("isFirstRunKey", true)
        if (isFirstRun){
            findNavController().navigate(R.id.action_menu_item_calories_to_menu_item_settings)
        }


    }
//    override fun onDestroy() {
//        super.onDestroy()
//        if (updateJob.isActive) {
//            updateJob.cancel()
//        }
//    }

}
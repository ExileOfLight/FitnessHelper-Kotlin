package com.example.fitnessfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfinal.databinding.FragmentCaloriesBinding
import com.example.fitnessfinal.db.FitnessDataBase
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
        val dao = FitnessDataBase.getInstance(requireContext()).userDao()
        val factory = FitnessViewModelFactory(dao)
        fitnessViewModel = ViewModelProvider(this, factory)[FitnessViewModel::class.java]

    }

    override fun onResume() {
        super.onResume()
        if (dbNotLoaded) {
            sharedViewModel.loadDataFromDB(this, fitnessViewModel)
            //sharedViewModel.loadDataFromDB(viewLifecycleOwner, userViewModel)
            updateJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(50)
                    sharedViewModel.updateMacros()
                    dbNotLoaded = false
            }

        } else sharedViewModel.updateMacros()


    }
    override fun onDestroy() {
        super.onDestroy()
        if (updateJob.isActive) {
            updateJob.cancel()
        }
    }

}
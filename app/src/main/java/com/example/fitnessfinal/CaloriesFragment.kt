package com.example.fitnessfinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfinal.databinding.FragmentCaloriesBinding
import com.example.fitnessfinal.databinding.FragmentSettingsBinding
import com.example.fitnessfinal.db.UserDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
class CaloriesFragment : Fragment() {

    private lateinit var binding: FragmentCaloriesBinding
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var userViewModel: UserViewModel
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
        val dao = UserDataBase.getInstance(requireContext()).userDao()
        val factory = UserViewModelFactory(dao)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

    }

    override fun onResume() {
        super.onResume()
        if (dbNotLoaded) {

            sharedViewModel.loadDataFromDB(viewLifecycleOwner, userViewModel)
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    sharedViewModel.updateMacros()
                }
            }
            dbNotLoaded = false
        } else sharedViewModel.updateMacros()


    }


}
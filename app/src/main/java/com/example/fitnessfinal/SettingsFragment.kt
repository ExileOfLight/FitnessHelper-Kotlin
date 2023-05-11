package com.example.fitnessfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfinal.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.apply { lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            return root }





    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.genderAsk.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_button_1 -> {
                    sharedViewModel.setisMale(true)
                }
                R.id.radio_button_2 -> {
                    sharedViewModel.setisMale(false)
                }
            }
        }
        sharedViewModel.updateMacros()
        binding.btnSubmit.setOnClickListener {
            binding.textView5.text = "HELLLOOOO"
            sharedViewModel.updateMacros()
        }
    }
}
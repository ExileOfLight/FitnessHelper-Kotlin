package com.example.fitnessfinal.fragmentsMain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessfinal.FitnessViewModel
import com.example.fitnessfinal.FitnessViewModelFactory
import com.example.fitnessfinal.R
import com.example.fitnessfinal.recyclerViewHandlers.MealRecyclerViewAdapter
import com.example.fitnessfinal.databinding.FragmentFoodBinding
import com.example.fitnessfinal.db.FitnessDataBase
import com.example.fitnessfinal.db.FitnessRepository


class FoodFragment : Fragment() {
    private lateinit var binding: FragmentFoodBinding
    //private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var fitnessViewModel: FitnessViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
//
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = FitnessDataBase.getInstance(requireContext()).fitnessDao()
        val factory = FitnessViewModelFactory(FitnessRepository(dao))
        fitnessViewModel = ViewModelProvider(this, factory)[FitnessViewModel::class.java]

        val adapter = MealRecyclerViewAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        fitnessViewModel.meals.observe(viewLifecycleOwner) { meal ->
            adapter.setData(meal)
        }
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_foodFragment_to_addFragment)
        }
    }

}
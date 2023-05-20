package com.example.fitnessfinal.recyclerViewHandlers

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessfinal.fragmentsMain.FoodFragmentDirections
import com.example.fitnessfinal.R
import com.example.fitnessfinal.databinding.ListItemBinding
import com.example.fitnessfinal.db.Meal


class MealRecyclerViewAdapter:RecyclerView.Adapter<MealRecyclerViewAdapter.MealViewHolder>()  {

    private var mealList = emptyList<Meal>()
    private lateinit var binding: ListItemBinding
    class MealViewHolder(binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MealViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val currentItem = mealList[position]
        binding.apply {
            tvName.text = currentItem.foodName
            tvCals.text = currentItem.cals.toString()
            tvProteins.text = currentItem.proteins.toString()
            tvFats.text = currentItem.fats.toString()
            tvCarbs.text = currentItem.carbs.toString()
            val colorOnActive = Color.CYAN
            val color = if (currentItem.amount>0) colorOnActive else Color.WHITE
            rowItemLayout.setCardBackgroundColor(color)

            rowItemLayout.setOnClickListener{ holder->
                val action = FoodFragmentDirections.actionFoodFragmentToUpdateFragment(currentItem)
                holder.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun setData(meals:List<Meal>){
        mealList = meals
        notifyDataSetChanged()
    }

}



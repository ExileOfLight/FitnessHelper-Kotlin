package com.example.fitnessfinal

import com.example.fitnessfinal.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessfinal.db.Meal

class MealRecyclerViewAdapter:RecyclerView.Adapter<MealViewHolder>()  {

    private var mealList = emptyList<Meal>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item,parent,false)
        return MealViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(mealList[position])
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun setData(meals:List<Meal>){
        mealList = meals
        notifyDataSetChanged()
    }

}


class MealViewHolder(private val view: View):RecyclerView.ViewHolder(view){
    fun bind(meal: Meal){
        val nameTextView = view.findViewById<TextView>(R.id.tvName)
        val calsTextView = view.findViewById<TextView>(R.id.tvCals)
        val proteinsTextView = view.findViewById<TextView>(R.id.tvProteins)
        val fatsTextView = view.findViewById<TextView>(R.id.tvFats)
        val carbsTextView = view.findViewById<TextView>(R.id.tvCarbs)
        nameTextView.text = meal.foodName.toString()
        calsTextView.text = meal.cals.toString()
        proteinsTextView.text = meal.proteins.toString()
        fatsTextView.text = meal.fats.toString()
        carbsTextView.text = meal.carbs.toString()

//        view.setOnClickListener {
//            clickListener(meal)
//        }
    }
}
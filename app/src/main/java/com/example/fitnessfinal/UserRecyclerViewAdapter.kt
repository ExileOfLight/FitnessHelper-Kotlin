package com.example.fitnessfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessfinal.db.User

class UserRecyclerViewAdapter(
    private val clickListener:(User)->Unit
):RecyclerView.Adapter<UserViewHolder>()  {

    private val userList = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item,parent,false)
        return UserViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setList(Users:List<User>){
        userList.clear()
        userList.addAll(Users)
    }

}


class UserViewHolder(private val view: View):RecyclerView.ViewHolder(view){
    fun bind(User: User,clickListener:(User)->Unit){
        val ageTextView = view.findViewById<TextView>(R.id.tvAge)
        val heightTextView = view.findViewById<TextView>(R.id.tvHeight)
        val weightTextView = view.findViewById<TextView>(R.id.tvWeight)
        val genderTextView = view.findViewById<TextView>(R.id.tvGender)
        ageTextView.text = User.age.toString()
        heightTextView.text = User.height.toString()
        weightTextView.text = User.weight.toString()
        genderTextView.text = User.gender
        view.setOnClickListener {
            clickListener(User)
        }
    }
}
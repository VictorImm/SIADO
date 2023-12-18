package com.example.siado.ui.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.siado.R
import com.example.siado.data.user.User
import com.example.siado.data.user.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class AttendanceAdapter(
    private val listUser: List<User>,
    private val viewModel: UserViewModel
): RecyclerView.Adapter<AttendanceAdapter.ListViewHolder>() {

    class ListViewHolder(typeView: View): RecyclerView.ViewHolder(typeView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvArrival: TextView = itemView.findViewById(R.id.tv_arrival)
        val tvGohome: TextView = itemView.findViewById(R.id.tv_gohome)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.row_attendance,
                    parent,
                    false
                )
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userArrival = listUser[position]

        holder.tvName.text = userArrival.name
        holder.tvArrival.text = "${userArrival.date}-${userArrival.mon}-${userArrival.year}:${userArrival.hour}.${userArrival.min}"
        viewModel.viewModelScope.launch {
            if (viewModel.isGohome(userArrival.name) == 1) {
                // if user is already gohome
                val userGohome = viewModel.getGohome(userArrival.name)
                holder.tvGohome.text = "${userGohome.date}-${userGohome.mon}-${userGohome.year}:${userGohome.hour}.${userGohome.min}"
            } else {
                holder.tvGohome.text = "-"
            }
        }
    }


}
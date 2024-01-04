package com.example.siado.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siado.R
import com.example.siado.data.application.Application
import com.example.siado.data.user.User
import com.example.siado.data.user.viewmodel.UserViewModel
import com.example.siado.data.user.viewmodel.UserViewModelFactory
import com.example.siado.databinding.FragmentAddUserBinding
import com.example.siado.databinding.FragmentAttendanceBinding
import com.example.siado.ui.admin.adapter.AttendanceAdapter

class AttendanceFragment : Fragment() {

    // binding
    private lateinit var binding: FragmentAttendanceBinding

    // widget
    private lateinit var rvAttendance: RecyclerView
    private lateinit var btnAddAttendance: Button
    private lateinit var btnAddDatabase: Button

    // viewModel
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            (activity?.application as Application).database.userDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init widget
        rvAttendance = binding.rvAttendance
        rvAttendance.setHasFixedSize(true)

        viewModel.allUser.observe(this.viewLifecycleOwner) {
            showRecyclerList(it)
        }

        btnAddAttendance = binding.btnAddAttendance
        btnAddAttendance.setOnClickListener {
            val action = AttendanceFragmentDirections.actionAttendanceFragmentToAddUserFragment()
            findNavController().navigate(action)
        }
    }

    private fun showRecyclerList(users: List<User>) {
        rvAttendance.layoutManager = LinearLayoutManager(this.context)

        val attendanceAdapter = AttendanceAdapter(users, viewModel)
        rvAttendance.adapter = attendanceAdapter
    }
}
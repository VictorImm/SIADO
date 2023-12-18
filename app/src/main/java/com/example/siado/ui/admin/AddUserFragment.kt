package com.example.siado.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.siado.R
import com.example.siado.databinding.FragmentAddUserBinding
import com.google.android.material.textfield.TextInputEditText

class AddUserFragment : Fragment() {

    // binding
    private lateinit var binding: FragmentAddUserBinding

    // widget
    private lateinit var inputName: TextInputEditText
    private lateinit var btnAdd: Button
    private lateinit var btnBack: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init widget
        inputName = binding.inputName

        btnAdd = binding.btnAdd
        btnAdd.setOnClickListener {
            // TODO: Save to database


            val action = AddUserFragmentDirections.actionAddUserFragmentToAttendanceFragment()
            findNavController().navigate(action)
        }

        btnBack = binding.btnBack
        btnBack.setOnClickListener {
            val action = AddUserFragmentDirections.actionAddUserFragmentToAttendanceFragment()
            findNavController().navigate(action)
        }
    }
}
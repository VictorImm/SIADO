package com.example.siado.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.siado.R
import com.example.siado.databinding.FragmentAddUserBinding
import com.example.siado.databinding.FragmentAttendanceBinding
import com.example.siado.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {

    // binding
    private lateinit var binding: FragmentLoginBinding

    // widget
    private lateinit var inputUname: TextInputEditText
    private lateinit var inputPass: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnBack: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // widget init
        inputUname = binding.inputUname
        inputPass = binding.inputPassword

        btnLogin = binding.btnLogin
        btnLogin.setOnClickListener {
            val username = inputUname.text.toString()
            val password = inputPass.text.toString()

            if (username == "admin" && password == "123") {
                // if password is correct, go to admin panel
                val action = LoginFragmentDirections.actionLoginFragmentToAttendanceFragment()
                findNavController().navigate(action)
            } else {
                // Input salah, tampilkan warna merah pada TextInputLayout
                if (username != "admin") {
                    binding.layoutInputUname.error = "Invalid username"
                } else {
                    binding.layoutInputUname.error = null
                }

                if (password != "123") {
                    binding.layoutInputPassword.error = "Invalid password"
                } else {
                    binding.layoutInputPassword.error = null
                }
            }
        }

        btnBack = binding.btnBack
        btnBack.setOnClickListener{
            // finish activity
        }

    }
}
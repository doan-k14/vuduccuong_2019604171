package com.example.qltaichinhcanhan.main.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentRegisterBinding
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class RegisterFragment : Fragment() {
    private lateinit var binding:FragmentRegisterBinding
    private lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]


        initView()
        initEvent()

    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textLogin.setOnClickListener {
            dataViewMode.checkInputScreenLogin = 1
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
        }
        binding.textSignUp.setOnClickListener {
            dataViewMode.checkInputScreenSignUp = 1
            findNavController().navigate(R.id.action_registerFragment_to_signUpFragment2)
        }
    }

    private fun initView() {
        binding.imgFb.setOnClickListener {
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG).show()
        }
        binding.imgGoogle.setOnClickListener {
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG).show()
        }
    }


}
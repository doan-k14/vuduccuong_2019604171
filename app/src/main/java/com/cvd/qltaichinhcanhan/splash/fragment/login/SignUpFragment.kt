package com.cvd.qltaichinhcanhan.splash.fragment.login

import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentSignupBinding
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.cvd.qltaichinhcanhan.utils.LoadingDialog
import com.cvd.qltaichinhcanhan.utils.Utils
import com.cvd.qltaichinhcanhan.utils.UtilsDialog
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {
    val TAG: String = "SignUpFragment"
    private lateinit var binding: FragmentSignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
        mAuth = FirebaseAuth.getInstance()
    }


    private fun initView() {
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.txtLoginNow.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.edtPass.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        val transformationMethod = PasswordTransformationMethod()

        binding.edtPass.transformationMethod = transformationMethod
        binding.imgEyePass.setOnClickListener {
            if (binding.edtPass.transformationMethod == transformationMethod) {
                binding.edtPass.transformationMethod = null
                binding.imgEyePass.isActivated = false

            } else {
                binding.edtPass.transformationMethod = transformationMethod
                binding.imgEyePass.isActivated = true
            }
        }

        binding.btnSignUp.setOnClickListener {
            checkDataSinUp()
        }

        binding.imgFb.setOnClickListener {
            Toast.makeText(
                requireActivity(),
                requireActivity().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG
            ).show()
        }
        binding.imgGoogle.setOnClickListener {
            Toast.makeText(
                requireActivity(),
                requireActivity().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkDataSinUp() {
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPass.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                requireActivity().resources.getString(R.string.please_enter_data),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (pass.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                requireActivity().resources.getString(R.string.please_enter_data),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        clickCreateAccount(email, pass)
    }

    private fun clickCreateAccount(email: String, pass: String) {
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    createUserAccount(email)
                } else {
                    Utils.showToast(requireContext(), "Tạo tài khoản thành công thất bại!")
                }
            }
    }

    private fun createUserAccount(email: String) {
        val utilsFireStore = UtilsFireStore()

        utilsFireStore.setCallBackCreateAccountUser(object :
            UtilsFireStore.CallBackCreateAccountUser {
            override fun createSuccess(idUserAccount: String) {
                getAccountMoneyByEmail(idUserAccount)
            }

            override fun createFailed() {

            }
        })

        utilsFireStore.createUserAccount(email)
    }

    private fun getAccountMoneyByEmail(idAccount: String) {
        val utilsFireStore = UtilsFireStore()
        utilsFireStore.setCBAccountMoneyByEmail(object : UtilsFireStore.CBAccountMoneyByEmail {
            override fun getSuccess() {
            }

            override fun getFailed() {
                findNavController().navigate(R.id.action_signUpFragment_to_creatsMoneyFragment)
            }
        })

        utilsFireStore.getAccountMoneyByEmail(idAccount)
    }
}
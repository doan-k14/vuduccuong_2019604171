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
import com.cvd.qltaichinhcanhan.main.model.m_new.Country
import com.cvd.qltaichinhcanhan.main.model.m_new.UserAccount
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.cvd.qltaichinhcanhan.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson


class SignUpFragment : Fragment() {
    val TAG: String = "SignUpFragment"
    private lateinit var binding: FragmentSignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dataViewMode: DataViewMode
    private lateinit var loadingDialog: LoadingDialog
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
        loadingDialog = LoadingDialog(requireContext())
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
        loadingDialog.showLoading()
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    createUserAccount(email)
                } else {
                    loadingDialog.hideLoading()
                    Utils.showToast(requireContext(), "Tạo tài khoản thành công thất bại!")
                }
            }
    }

    private fun createUserAccount(email: String) {
        val utilsFireStore = UtilsFireStore()

        utilsFireStore.setCallBackCreateAccountUser(object :
            UtilsFireStore.CallBackCreateAccountUser {
            override fun createSuccess(idUserAccount: String) {
                val userAccount = UserAccount(idUserAccount,email,countryDefault = Country())
                val gson = Gson()
                val stringUserAccount = gson.toJson(userAccount)
                Utils.putString(requireContext(),Constant.USER_LOGIN_SUCCESS,stringUserAccount)
                Utils.putBoolean(requireContext(),Constant.LOGIN_SUCCESS,true)
                loadingDialog.hideLoading()
                findNavController().navigate(R.id.action_signUpFragment_to_creatsMoneyFragment)
            }


            override fun createFailed() {

            }
        })

        utilsFireStore.createUserAccount(email)
    }
}
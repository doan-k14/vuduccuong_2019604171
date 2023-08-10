package com.cvd.qltaichinhcanhan.splash.fragment.login

import android.content.Intent
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
import com.cvd.qltaichinhcanhan.databinding.FragmentLoginBinding
import com.cvd.qltaichinhcanhan.main.NDMainActivity
import com.cvd.qltaichinhcanhan.main.model.m_new.UserAccount
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.cvd.qltaichinhcanhan.utils.*
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dataViewMode: DataViewMode
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        loadingDialog = LoadingDialog(requireContext())
        initView()
        initEvent()
    }

    private fun initView() {
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
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        auth = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            checkDataLogin()
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
        binding.textForgotPass.setOnClickListener {
            Toast.makeText(
                requireActivity(),
                requireActivity().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkDataLogin() {
        val email = binding.edtAccount.text.toString()
        val pass = binding.edtPass.text.toString()
        if (email.isEmpty() || !Utils.isValidGmail(email)) {
            Toast.makeText(
                requireActivity(),
                requireActivity().resources.getString(R.string.enter_email),
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
        login(email, pass)
    }

    private fun login(email: String, pass: String) {
        loadingDialog.showLoading()
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Utils.putBoolean(requireContext(), Constant.LOGIN_SUCCESS, true)

                    val utilsFireStore = UtilsFireStore()
                    utilsFireStore.setCBUserAccountLogin(object :
                        UtilsFireStore.CBUserAccountLogin {
                        override fun getSuccess(userAccount: UserAccount) {
                            Utils.saveUserAccountLogin(requireContext(),userAccount)
                            utilsFireStore.getAccountMoneyByEmail(userAccount.idUserAccount.toString())
                        }

                        override fun getFailed() {
                            loadingDialog.hideLoading()
                        }
                    })
                    utilsFireStore.getUserAccountLogin(email)

                    utilsFireStore.setCBAccountMoneyByEmail(object :
                        UtilsFireStore.CBAccountMoneyByEmail {
                        override fun getSuccess() {
                            loadingDialog.hideLoading()
                            val intent = Intent(requireActivity(), NDMainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        override fun getFailed() {
                            loadingDialog.hideLoading()
                            findNavController().navigate(R.id.action_splashFragment_to_creatsMoneyFragment)
                        }
                    })

                } else {

                    loadingDialog.hideLoading()
                    Toast.makeText(
                        requireActivity(),
                        "Đăng nhập thất bại! Hãy đăng nhập lại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.edtAccount.setText("")
        binding.edtPass.setText("")
        dataViewMode.checkInputScreenLogin = 0
    }
}
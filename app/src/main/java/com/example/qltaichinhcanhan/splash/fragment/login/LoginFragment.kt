package com.example.qltaichinhcanhan.splash.fragment.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentLoginBinding
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dataViewMode: DataViewMode

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

        val check = dataViewMode.checkInputScreenLogin
        if (check == 0) {
            binding.clActionBarTop.visibility = View.GONE
            binding.textForgotPass.visibility = View.VISIBLE
            binding.textSignUp.visibility = View.VISIBLE
            binding.llOrLogin.visibility = View.VISIBLE
            binding.llOtherLogin.visibility = View.VISIBLE
            binding.imageLogo.visibility = View.VISIBLE
            binding.textName.visibility = View.VISIBLE
        } else {
            binding.clActionBarTop.visibility = View.VISIBLE
            binding.textForgotPass.visibility = View.GONE
            binding.textSignUp.visibility = View.GONE
            binding.llOrLogin.visibility = View.GONE
            binding.llOtherLogin.visibility = View.GONE
            binding.imageLogo.visibility = View.GONE
            binding.textName.visibility = View.INVISIBLE

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
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG).show()
        }
        binding.imgGoogle.setOnClickListener {
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG).show()
        }
        binding.textForgotPass.setOnClickListener {
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG).show()
        }
    }

    private fun checkDataLogin() {
        val email = binding.edtAccount.text.toString()
        val pass = binding.edtPass.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.please_enter_data),
                Toast.LENGTH_LONG).show()
            return
        }
        if (pass.isEmpty()) {
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.please_enter_data),
                Toast.LENGTH_LONG).show()
            return
        }
        binding.pressedLoading.visibility = View.VISIBLE
        login(email, pass)
    }

    private fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val name = email.split("@")[0]
                    val accountNew = Account(1, name, email, pass, "null", false)
                    val list = dataViewMode.listAccount
                    val account = list.find { it.emailName == email }

                    binding.edtAccount.setText("")
                    binding.edtPass.setText("")
                    if (dataViewMode.checkInputScreenLogin == 0) {
                        if (account != null) {
                            account.selectAccount = true
                            dataViewMode.updateAccount(account)
                            lifecycleScope.launch {
                                delay(100)
                                val intent = Intent(requireActivity(), NDMainActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        } else {
                            dataViewMode.accountLogin = accountNew
                            dataViewMode.addAccount(accountNew)
                            dataViewMode.checkInputScreenCreateMoney = 0
                            binding.pressedLoading.visibility = View.GONE
                            findNavController().navigate(R.id.action_loginFragment_to_creatsMoneyFragment)
                        }
                    } else {
                        accountNew.selectAccount = true
                        dataViewMode.addAccount(accountNew)
                        binding.pressedLoading.visibility = View.GONE
                        dataViewMode.checkGetAccountLoginHome = 1
                        findNavController().popBackStack(R.id.nav_home, false)
                    }
                } else {
                    Toast.makeText(requireActivity(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT)
                        .show()
                    binding.pressedLoading.visibility = View.GONE
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataViewMode.checkInputScreenLogin = 0
    }

    private fun checkCreateMoneyAccount(account: Account) {
        dataViewMode.getMoneyAccountMainByIdAccount(account.idAccount)
        dataViewMode.moneyAccountMainByIdAccount.observe(requireActivity()) {
            if (it != null) {
                lifecycleScope.launch {
                    delay(10)
                    val intent = Intent(requireActivity(), NDMainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            } else {
                lifecycleScope.launch {
                    delay(10)
                    findNavController().navigate(R.id.action_loginFragment_to_creatsMoneyFragment)
                }
            }
        }
    }
}
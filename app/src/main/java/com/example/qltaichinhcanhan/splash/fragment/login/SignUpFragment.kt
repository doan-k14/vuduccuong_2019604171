package com.example.qltaichinhcanhan.splash.fragment.login

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
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentSignupBinding
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private val RC_SIGN_IN = 9001
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
        val check = dataViewMode.checkInputScreenSignUp
        if (check == 0) {
            binding.clActionBarTop.visibility = View.GONE
            binding.llHaveHad.visibility = View.VISIBLE
            binding.llOrLogin.visibility = View.VISIBLE
            binding.llOtherLogin.visibility = View.VISIBLE
            binding.imageLogo.visibility = View.VISIBLE
            binding.textName.visibility = View.VISIBLE

        } else {
            binding.clActionBarTop.visibility = View.VISIBLE
            binding.llHaveHad.visibility = View.GONE
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

    private fun checkDataSinUp() {
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPass.text.toString()
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
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
        if (firstName.isEmpty()) {
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.error_first_name),
                Toast.LENGTH_LONG).show()
            return
        }
        if (lastName.isEmpty()) {
            Toast.makeText(requireActivity(),
                requireActivity().resources.getString(R.string.error_last_name),
                Toast.LENGTH_LONG).show()
            return
        }
        binding.pressedLoading.visibility = View.VISIBLE
        createAccount(email, pass, firstName, lastName)
    }

    fun createAccount(email: String, pass: String, firstName: String, lastName: String) {
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val name = "$firstName $lastName"
                    val account = Account(0, name, email, pass, "null", false)
                    if (dataViewMode.checkInputScreenSignUp == 0) {
                        dataViewMode.addAccount(account)
                        binding.pressedLoading.visibility = View.GONE
                        dataViewMode.checkInputScreenCreateMoney = 1
                        findNavController().navigate(R.id.action_signUpFragment_to_creatsMoneyFragment)
                    } else {
                        account.selectAccount = true
                        dataViewMode.addAccount(account)
                        dataViewMode.checkGetAccountLoginHome = 0
                        findNavController().popBackStack(R.id.nav_home, false)
                    }
                } else {
                    binding.pressedLoading.visibility = View.GONE
                    Toast.makeText(requireActivity(),
                        "Tạo tài khoản thành công thất bại!",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resertView()
        dataViewMode.checkInputScreenSignUp = 0
    }

    fun resertView() {
        binding.edtEmail.setText("")
        binding.edtPass.setText("")
        binding.edtFirstName.setText("")
        binding.edtLastName.setText("")
        binding.edtPhoneNumber.setText("")
        binding.pressedLoading.visibility = View.GONE
    }

    // Đăng nhập Firebase bằng token của Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Đăng nhập thành công
                    Log.e("ttt", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    // Cập nhật UI với thông tin người dùng đăng nhập thành công
                } else {
                    // Đăng nhập thất bại
                    Log.e("ttt", "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireActivity(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


//    // đăng nhập
//    private fun signInWithGoogle() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
//
//        val signInIntent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Đăng nhập Google thành công
//                val account = task.getResult(ApiException::class.java)
//                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
//                mAuth.signInWithCredential(credential)
//                    .addOnCompleteListener(requireActivity()) { task ->
//                        if (task.isSuccessful) {
//                            // Đăng nhập Firebase thành công
//                            val user = mAuth.currentUser
//                            // Tiến hành xử lý tiếp
//                            Toast.makeText(requireActivity(), "Đăng nhập ", Toast.LENGTH_SHORT).show()
//
//                        } else {
//                            // Đăng nhập Firebase thất bại
//                            Toast.makeText(requireActivity(), "Đăng nhập thất bại1", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            } catch (e: ApiException) {
//                // Đăng nhập Google thất bại
//                Log.e("ttt", "Đăng nhập Google thất bại: " + e.statusCode)
//                Toast.makeText(requireActivity(), "Đăng nhập thất bại2", Toast.LENGTH_SHORT).show()
//            }
//
//        }
//    }


}
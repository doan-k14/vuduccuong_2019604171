package com.cvd.qltaichinhcanhan.splash.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cvd.qltaichinhcanhan.AdminActivity
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.main.NDMainActivity
import com.cvd.qltaichinhcanhan.main.model.m_new.UserAccount
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.cvd.qltaichinhcanhan.utils.Constant
import com.cvd.qltaichinhcanhan.utils.UtilsSharedP
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore
import com.google.gson.Gson
import kotlinx.coroutines.*


class SplashFragment : Fragment() {
    lateinit var dataViewMode: DataViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(this)[DataViewMode::class.java]
        checkLogin()
    }

    private fun checkLogin() {
        if (UtilsSharedP.getBoolean(requireContext(), Constant.FIRST_OPEN_APP)) {
            if (UtilsSharedP.getBoolean(requireContext(), Constant.LOGIN_SUCCESS)) {
                if (UtilsSharedP.getBoolean(requireContext(), Constant.PERMISSION_ADMIN)) {
                    val intent = Intent(requireActivity(), AdminActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    val userAccount = UtilsSharedP.getUserAccountLogin(requireContext())
                    if (userAccount.countryDefault != null) {
                        val intent = Intent(requireActivity(), NDMainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_creatsMoneyFragment)
                    }
                }
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }

        } else {
            UtilsSharedP.putBoolean(requireContext(), Constant.FIRST_OPEN_APP, true)
            lifecycleScope.launch {
                delay(10)
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }
        }
    }
}

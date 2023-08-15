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
import com.cvd.qltaichinhcanhan.utils.Utils
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
//        val utilsFireStore = UtilsFireStore()
//            utilsFireStore.pushListCountry()

        checkLogin()
    }

    private fun checkLogin() {
        Log.e("TAG", "checkCreateMoneyAccount: "+ Utils.getBoolean(requireContext(), Constant.FIRST_OPEN_APP))
        if (Utils.getBoolean(requireContext(), Constant.FIRST_OPEN_APP)) {
            if (Utils.getBoolean(requireContext(), Constant.LOGIN_SUCCESS)) {
                val stringUserAccount = Utils.getString(requireContext(), Constant.USER_LOGIN_SUCCESS)
                val gson = Gson()
                val userAccount = gson.fromJson(stringUserAccount, UserAccount::class.java)
                if(checkAdmin(userAccount)){
                    Utils.putBoolean(requireContext(),Constant.PERMISSION_ADMIN,true)
                    val intent = Intent(requireActivity(), AdminActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }else{
                    val utilsFireStore = UtilsFireStore()
                    utilsFireStore.setCBAccountMoneyByEmail(object : UtilsFireStore.CBAccountMoneyByEmail {
                        override fun getSuccess() {
                            val intent = Intent(requireActivity(), NDMainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        override fun getFailed() {
                            findNavController().navigate(R.id.action_splashFragment_to_creatsMoneyFragment)
                        }
                    })
                    utilsFireStore.getAccountMoneyByEmail(userAccount.idUserAccount.toString())
                }
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }

        } else {
            Utils.putBoolean(requireContext(), Constant.FIRST_OPEN_APP,true)
            lifecycleScope.launch {
                delay(10)
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }
        }
    }


}

    private fun checkAdmin(userAccount: UserAccount): Boolean {
        if(userAccount.email == "vuduccuong1503@gmail.com"){
            return true
        }
        return  false
    }

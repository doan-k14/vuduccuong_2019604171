package com.cvd.qltaichinhcanhan.splash.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.main.NDMainActivity
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.cvd.qltaichinhcanhan.utils.Constant
import com.cvd.qltaichinhcanhan.utils.Utils
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
        checkCreateMoneyAccount()
    }

    private fun checkCreateMoneyAccount() {
        if (Utils.getBoolean(requireContext(), Constant.CREATE_MONEY_ACCOUNT)) {
            lifecycleScope.launch {
                delay(10)
                val intent = Intent(requireActivity(), NDMainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            lifecycleScope.launch {
                delay(10)
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }
        }
    }


}
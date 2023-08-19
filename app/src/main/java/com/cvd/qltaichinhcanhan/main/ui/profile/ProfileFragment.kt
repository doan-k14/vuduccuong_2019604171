package com.cvd.qltaichinhcanhan.main.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentProfileBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.CustomDialog
import com.cvd.qltaichinhcanhan.main.model.m_r.Account
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.cvd.qltaichinhcanhan.splash.OnBoardingScreenActivity
import com.cvd.qltaichinhcanhan.utils.Constant
import com.cvd.qltaichinhcanhan.utils.UtilsSharedP


class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding
    lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.nav_home, false)
        }
        initView()
        initEvent()
    }

    private fun initView() {
        val userAccount = UtilsSharedP.getUserAccountLogin(requireContext())
        if (userAccount.idUserAccount != null) {
            binding.textNameEmail.text = userAccount.email
        }
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textExit.setOnClickListener {
            val customDialog = CustomDialog(requireActivity())
            customDialog.showDialog(
                Gravity.CENTER,
                resources.getString(R.string.dialog_message),
                resources.getString(R.string.mess_exit),
                resources.getString(R.string.text_ok),
                {
                    customDialog.dismiss()
                    UtilsSharedP.putBoolean(requireContext(), Constant.LOGIN_SUCCESS, false)
                    val intent = Intent(requireActivity(), OnBoardingScreenActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                },
                resources.getString(R.string.text_no),
                {
                    customDialog.dismiss()
                }
            )
        }

    }

    private fun createDialogConfirmDeleteData() {
        val customDialog = CustomDialog(requireActivity())
        customDialog.showDialog(
            Gravity.CENTER,
            resources.getString(R.string.dialog_message),
            resources.getString(R.string.mess_confrim_delete_data),
            resources.getString(R.string.text_ok),
            {
                // xoa all data lên cần nhập mk
                customDialog.dismiss()

                UtilsSharedP.putBoolean(requireContext(), Constant.CREATE_MONEY_ACCOUNT, false)
                dataViewMode.deleteAllData()

                val intent = Intent(requireActivity(), OnBoardingScreenActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            },
            resources.getString(R.string.text_no),
            {
                customDialog.dismiss()
            }
        )

    }

    private fun deleteAccount(account: Account) {
        val intent = Intent(requireActivity(), OnBoardingScreenActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}
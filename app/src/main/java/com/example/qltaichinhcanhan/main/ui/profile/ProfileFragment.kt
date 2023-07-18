package com.example.qltaichinhcanhan.main.ui.profile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentProfileBinding
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.library.CustomDialog
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.example.qltaichinhcanhan.splash.OnBoardingScreenActivity
import com.example.qltaichinhcanhan.utils.Constant
import com.example.qltaichinhcanhan.utils.Utils


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
        val accountDefault = dataViewMode.accountLoginHome
        if (accountDefault.idAccount != 0) {
            binding.textNameAccount.text = accountDefault.accountName
            binding.textNameEmail.text = accountDefault.emailName
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
                    createDialogConfirmDeleteData();
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

                Utils.putBoolean(requireContext(), Constant.CREATE_MONEY_ACCOUNT, false)
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
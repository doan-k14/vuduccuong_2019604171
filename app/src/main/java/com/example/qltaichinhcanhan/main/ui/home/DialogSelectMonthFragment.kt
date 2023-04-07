package com.example.qltaichinhcanhan.main.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.FragmentDialogSelectMothBinding
import com.example.qltaichinhcanhan.main.adapter.MonthAdapter
import com.example.qltaichinhcanhan.main.model.m.DefaultData
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class DialogSelectMonthFragment : DialogFragment() {

    lateinit var binding: FragmentDialogSelectMothBinding
    lateinit var mothAdapter: MonthAdapter
    lateinit var dataViewMode: DataViewMode

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window!!
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wLayoutParams = window.attributes
        window.attributes = wLayoutParams
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDialogSelectMothBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {

        val time = dataViewMode.timeSelectMoth
        val parts = time.split("/")
        var timeMoth = parts[0].toInt()
        var timeYear = parts[1].toInt()

        binding.textMoth.text = "Tháng $timeMoth Năm $timeYear"

        binding.textYear.text = timeYear.toString()
        mothAdapter = MonthAdapter(requireActivity(), DefaultData.listMothR)
        binding.rcvMoth.adapter = mothAdapter

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 4, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

        binding.rcvMoth.layoutManager = myLinearLayoutManager1

        mothAdapter.updateSelect(timeMoth)
        binding.imgLeft.setOnClickListener {
            timeYear--
            binding.textYear.text = timeYear.toString()
            binding.textMoth.text =
                "Tháng " + timeMoth + " Năm " + timeYear
        }
        binding.imgRight.setOnClickListener {
            timeYear++
            binding.textYear.text = timeYear.toString()
            binding.textMoth.text =
                "Tháng " + timeMoth + " Năm " + timeYear
        }

        mothAdapter.setClickItemSelect {
            timeMoth = it.idMothR!!.toInt()
            binding.textMoth.text =
                "Tháng " + timeMoth + " Năm " + timeYear
        }
        binding.textCancel.setOnClickListener {
            dismiss()
        }
        binding.textOk.setOnClickListener {
            dataViewMode.timeSelectMoth = "$timeMoth/$timeYear"
            dataViewMode.setIsChecked(true)
            dismiss()
        }
    }
}

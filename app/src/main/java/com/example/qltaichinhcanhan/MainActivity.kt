package com.example.qltaichinhcanhan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.qltaichinhcanhan.inf.FragmentADelegate
import com.example.qltaichinhcanhan.adapter.FragmentAdapter
import com.example.qltaichinhcanhan.databinding.ActivityMainBinding
import com.example.qltaichinhcanhan.fragment.DetailMoneyFragment
import com.example.qltaichinhcanhan.fragment.EditMoneyFragment
import com.example.qltaichinhcanhan.fragment.ReportFragment
import com.example.qltaichinhcanhan.fragment.SummaryFragment
import com.example.qltaichinhcanhan.inf.CallBackEdtMoney
import com.example.qltaichinhcanhan.inf.InterDetailToReport
import com.example.qltaichinhcanhan.mode.Money
import com.google.android.material.tabs.TabLayoutMediator
import java.io.Serializable

class MainActivity : AppCompatActivity(), FragmentADelegate,CallBackEdtMoney {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterPager: FragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setColorStatusbar()
        initView()
    }

    private fun initView() {
        adapterPager = FragmentAdapter(this)
        binding.viewPagerMain.adapter = adapterPager

        TabLayoutMediator(
            binding.tabLayoutMain,
            binding.viewPagerMain
        ) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.report)
                1 -> tab.text = resources.getString(R.string.add)
                2 -> tab.text = resources.getString(R.string.summary)
            }
        }.attach()
    }

    fun setColorStatusbar() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.main_color)
        window.navigationBarColor = resources.getColor(R.color.main_color)
    }

    // nhận call từ report -> detail , dữ liệu
    override fun showFragmentDetailMoney(money: Money) {
        val detailMoneyFragment = DetailMoneyFragment()
        val bundle = Bundle()
        bundle.putSerializable("data", money as Serializable)
        detailMoneyFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.ll_container, detailMoneyFragment,"detail_money_fragment")
            .addToBackStack(null)
            .commit()
    }

    override fun backToFragmentA() {
        supportFragmentManager.popBackStack()
    }

    override fun showFragmentEditMoney(money: Money) {
        val editMoneyFragment = EditMoneyFragment()
        val bundle = Bundle()
        bundle.putSerializable("data", money as Serializable)
        editMoneyFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.ll_container,editMoneyFragment,"edit_money_fragment" )
            .addToBackStack(null)
            .commit()
    }

    override fun updateMoney(money: Money) {
        val fragmentB = supportFragmentManager.findFragmentByTag("detail_money_fragment") as DetailMoneyFragment?
        fragmentB?.setDataMoney(money)
        supportFragmentManager.popBackStack()
    }


//    override fun deleteMoneySuccess() {
//        val fragmentA = supportFragmentManager.findFragmentByTag("android:switcher:${binding.viewPagerMain.id}:${0}")
//
//
//    }






}
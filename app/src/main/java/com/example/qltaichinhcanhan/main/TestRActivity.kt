package com.example.qltaichinhcanhan.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.qltaichinhcanhan.databinding.ActivityTestRactivityBinding
import com.example.qltaichinhcanhan.main.model.m_r.*
import com.example.qltaichinhcanhan.main.rdb.vm_data.AccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.MoneyAccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.TransactionViewMode

class TestRActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestRactivityBinding
    lateinit var accountViewMode: AccountViewMode
    lateinit var countryViewMode: CountryViewMode
    lateinit var moneyAccountViewMode: MoneyAccountViewMode
    lateinit var transactionViewMode: TransactionViewMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestRactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accountViewMode = ViewModelProvider(this)[AccountViewMode::class.java]
        countryViewMode = ViewModelProvider(this)[CountryViewMode::class.java]
        moneyAccountViewMode = ViewModelProvider(this)[MoneyAccountViewMode::class.java]
        transactionViewMode = ViewModelProvider(this)[TransactionViewMode::class.java]
        initData()

    }

    private fun initData() {

        binding.btAddAccount.setOnClickListener {
            accountViewMode.addAccount(Account(0, "Vu Duc Cuong", "12345", "image", false))
        }
        binding.btAddCountry.setOnClickListener {
            countryViewMode.addAccount(Country(0,
                "Vu Duc Cuong",
                "12345",
                "image",
                "$",
                "da",
                33F,
                false))
        }
var moneyAccount = MoneyAccount()
        binding.btAddMoneyAccount.setOnClickListener {

//            moneyAccountViewMode.addAccount(MoneyAccount(0, "Vu Duc Cuong", 122222F,false,1,1,1,1))

            moneyAccountViewMode.getMoneyAccountWithDetails(2).toString()
            moneyAccountViewMode.moneyAccountWithDetailsId.observe(this) {
                Log.e("data", "ok: - ${it[0].moneyAccount.toString()}")
                moneyAccount = it[0].moneyAccount
            }

            moneyAccountViewMode.getAllMoneyAccountsWithDetails()
            moneyAccountViewMode.moneyAccountsWithDetails.observe(this) {
                for (i in it) {
                    Log.e("data", "${i.toString()}")
                }
            }

        }

        binding.btAddColor.setOnClickListener {
            moneyAccount.moneyAccountName = "new"
            moneyAccountViewMode.updateAccount(moneyAccount)
        }

    }
}
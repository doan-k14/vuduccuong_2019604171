package com.cvd.qltaichinhcanhan.utils

import android.content.Context
import com.cvd.qltaichinhcanhan.main.model.m_new.Country
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoadingData {

    interface CBListCountry {
        fun getListSuccess(list: List<Country>)
        fun getListFailed()
    }

    private lateinit var cBListCountry: CBListCountry

    fun setCBListCountry(cBListCountry: CBListCountry) {
        this.cBListCountry = cBListCountry
    }

    fun loadingCountry(context: Context) {
        val country = UtilsSharedP.getCountryDefault(context)

        val database = FirebaseDatabase.getInstance()
        val countriesReference = database.getReference("countries")

        countriesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val countryList = mutableListOf<Country>()

                for (countrySnapshot in snapshot.children) {
                    val country = countrySnapshot.getValue(Country::class.java)
                    if (country != null) {
                        countryList.add(country)
                    }
                }
                if (countryList.size != 0) {
                    val listCountry = checkConvertExchangeRate(countryList, country)
                    if (listCountry.isNotEmpty()) {
                        cBListCountry.getListSuccess(countryList)
                    } else {
                        cBListCountry.getListFailed()
                    }
                } else {
                    cBListCountry.getListFailed()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cBListCountry.getListFailed()
            }
        })

    }

    private fun checkConvertExchangeRate(list: List<Country>, country: Country): List<Country> {
        return if (country.idCountry != null) {
            convertCurrencyBySelectedCountry(list, country.idCountry)
        } else {
            list
        }
    }

    private fun convertCurrencyBySelectedCountry(
        countryList: List<Country>,
        selectedCountryId: Int
    ): List<Country> {
        val selectedCountry = countryList.find { it.idCountry == selectedCountryId }

        if (selectedCountry != null) {
            val selectedExchangeRate = selectedCountry.exchangeRate ?: 1f

            return countryList.map { country ->
                val convertedRate = if (country.idCountry == selectedCountryId) {
                    1f
                } else {
                    (country.exchangeRate ?: 1f) / selectedExchangeRate
                }

                country.copy(exchangeRate = convertedRate)
            }
        }

        return countryList
    }


}
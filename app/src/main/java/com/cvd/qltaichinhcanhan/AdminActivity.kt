package com.cvd.qltaichinhcanhan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cvd.qltaichinhcanhan.databinding.ActivityAdminBinding
import com.cvd.qltaichinhcanhan.main.model.m_new.Country
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val url =
//            "https://sbv.gov.vn/TyGia/faces/TyGiaCheoMobile.jspx"
//        binding.webView.loadUrl(url)


        var listCountry = listOf<Country>()
        val utilsFireStore  = UtilsFireStore()

        utilsFireStore.setCBListCountry(object :UtilsFireStore.CBListCountry{
            override fun getListSuccess(list: List<Country>) {
                listCountry = list
                updateExchange(list)

            }

            override fun getListFailed() {
                TODO("Not yet implemented")
            }
        })

//        utilsFireStore.getListCountry()




    }

    private fun updateExchange(list: List<Country>) {
        val url = "https://portal.vietcombank.com.vn/Usercontrols/TVPortal.TyGia/pXML.aspx"

        Thread {
            try {
                val document: Document = Jsoup.connect(url).get()

                val exrateElements = document.select("Exrate")

                for (element in exrateElements) {
                    val currencyName = element.attr("CurrencyName")
                    val exchangeRate = element.attr("Transfer").replace(",", "").toFloat()

                    val countryToUpdate = list.find { it.countryName == currencyName }
                    countryToUpdate?.exchangeRate = exchangeRate

                }
                runOnUiThread {

                    displayData(list)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun displayData(dataList: List<Country>) {
        for(i in dataList){
            Log.e("TAG", "displayData: "+i.toString() )
        }
    }
}
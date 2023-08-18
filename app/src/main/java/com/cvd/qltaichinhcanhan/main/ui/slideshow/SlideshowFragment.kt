package com.cvd.qltaichinhcanhan.main.ui.slideshow

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.*
import com.cvd.qltaichinhcanhan.databinding.FragmentSlideshowBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.n_adapter.InAppPurchaseAdapter
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.google.common.collect.ImmutableList

class SlideshowFragment : BaseFragment() {

    lateinit var binding: FragmentSlideshowBinding
    lateinit var dataViewMode: DataViewMode
    lateinit var inAppPurchaseAdapter: InAppPurchaseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        createINAP()

        initEvent()
    }

    private fun initEvent() {
        inAppPurchaseAdapter.setItemClickListener(object :
            InAppPurchaseAdapter.OnItemClickListener {
            override fun onItemClick(appVC: SkuDetails?) {
                initIAPGG(appVC!!.sku)
            }
        })
    }

    private fun initView() {
        inAppPurchaseAdapter = InAppPurchaseAdapter(requireContext(), listOf())
        val layout = GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, false)
        binding.rcvInAppPurchase.adapter = inAppPurchaseAdapter
        binding.rcvInAppPurchase.layoutManager = layout
    }

    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    private fun createINAP() {
        // khởi tạo
        billingClient = BillingClient.newBuilder(requireContext())
            .enablePendingPurchases()
            .setListener { billingResult: BillingResult?, list: List<Purchase?>? ->
                //Trả về kết quả khi người dùng thực hiện mua hàng.
                onPurchasesUpdated(billingResult!!, list as List<Purchase>?)
            }
            .build()

        // Connect ứng dụng của bạn với Google Billing

        // Connect ứng dụng của bạn với Google Billing
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // lấy ds các gói đã mua
                    queryHistory()

                    // lấy danh sách các gói iap của app trên gpc
                    queryProfileProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                //TODO: Connect Google Play not success
            }
        })
    }

    private lateinit var billingClient: BillingClient

    // Phương thức để truy vấn lịch sử các giao dịch mua hàng trước đó (purchase history).INAPP (in-app products).
    private fun queryHistory() {
        try {
            billingClient.queryPurchaseHistoryAsync(
                QueryPurchaseHistoryParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP).build()
            ) { billingResult, list ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list != null) {
                    var coin = 0
                    var subs = ""
                    for (i in list) {
                        // Kiểm tra purchasedSku để xác định gói đã mua
                        if (i.skus.toString() == "[vdc_mm_01]") {
                            coin += 10000
                        } else if (i.skus.toString() == "[vdc_mm_02]") {
                            coin += 20000
                        } else if (i.skus.toString() == "[vdc_mm_03]") {
                            coin += 30000
                        }else if (i.skus.toString() == "[vdc_mm_03]") {
                            coin += 30000
                        } else if (i.skus.toString() == "[vdc_mm_subs_01]") {
                            subs += "3 tháng"
                        }
                        Log.e("TAG", "queryPurchased, id iap: " + i.skus)
//                            consumeAsync(historyRecord.purchaseToken)
                    }
                    binding.txtCoin.setText("Coin: $coin"+" : "+subs)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // (Consume) Việc tiêu thụ giao dịch đảm bảo rằng người dùng không thể mua lại một lần nữa các sản phẩm đã mua.
    private fun consumeAsync(purchaseToken: String) {
        try {
            val listener =
                ConsumeResponseListener { billingResult, purchaseToken -> }
            val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build()
            if (billingClient != null) {
                billingClient.consumeAsync(consumeParams, listener)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun queryProfileProducts() {
        val productIds: MutableList<String> = ArrayList()
        productIds.add("vdc_mm_01")
        productIds.add("vdc_mm_02")
        productIds.add("vdc_mm_03")
        productIds.add("vdc_mm_subs_01")
        productIds.add("mm-month-3")

        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(productIds)
            .setType(BillingClient.SkuType.INAPP) //Sử dụng INAPP với one-time product và SUBS với các gói subscriptions.
            .build()
        billingClient.querySkuDetailsAsync(skuDetailsParams) { _: BillingResult?, list: List<SkuDetails?>? ->
            if (list != null) {
                requireActivity().runOnUiThread { inAppPurchaseAdapter.updateData(list as List<SkuDetails>) }
            }
        }
    }


    private fun launchBillingFlow(productDetails: ProductDetails) {
        try {
            var offerToken = ""
            if (productDetails.subscriptionOfferDetails != null) {
                offerToken = productDetails
                    .subscriptionOfferDetails!![0]
                    .offerToken
            }
            val productDetailsParamsList: ImmutableList<BillingFlowParams.ProductDetailsParams> =
                ImmutableList.of(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken)
                        .build()
                )
            if (billingClient != null) {
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                billingClient.launchBillingFlow(requireActivity(), flowParams)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Khi user click vào Buy trong màn hình mua hàng của Google Play
    fun onPurchasesUpdated(billingResult: BillingResult, list: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            Toast.makeText(requireContext(), "Buy product success", Toast.LENGTH_SHORT).show()
            if (list != null) {
                for (purchase in list) {
                    val purchasedSku = purchase.skus.toString()
                    Log.e("TAG", "onPurchasesUpdated: mua hàng thành công: " + purchasedSku.toString())
                    // Kiểm tra purchasedSku để xác định gói đã mua
                    var coin = 0
                    if (purchasedSku == "vdc_mm_01") {
                        coin += 10000
                    } else if (purchasedSku == "vdc_mm_02") {
                        coin += 20000
                    } else if (purchasedSku == "vdc_mm_03") {
                        coin += 30000
                    }
                    binding.txtCoin.setText("Coin: $coin")
//                    handlePurchase(purchase)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Buy product false", Toast.LENGTH_SHORT).show()
        }
    }

    fun handlePurchase(purchase: Purchase) {
        handleConsumableProduct(purchase)
        handleNonConsumableProduct(purchase)
    }

    fun handleConsumableProduct(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.consumeAsync(consumeParams,
            ConsumeResponseListener { billingResult: BillingResult, purchaseToken: String? ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                    // Xử lý sự thành công của hoạt động tiêu thụ.
                }
            })
    }

    fun handleNonConsumableProduct(purchase: Purchase) {
        if (purchase.purchaseState == purchase.purchaseState) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams,
                    AcknowledgePurchaseResponseListener { billingResult: BillingResult? -> })
            }
        }
    }

    fun initIAPGG(skuId: String) {

        val productList: ImmutableList<QueryProductDetailsParams.Product> = ImmutableList.of(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(skuId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        billingClient.queryProductDetailsAsync(
            params,
            ProductDetailsResponseListener { billingResult, productDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (productDetailsList.size == 0) {

                        return@ProductDetailsResponseListener
                    }
                    for (productDetails in productDetailsList) {
                        if (productDetails.productId == skuId) {
                            launchBillingFlow(productDetails)
                        }
                    }
                } else {

                }
            }
        )
    }


}


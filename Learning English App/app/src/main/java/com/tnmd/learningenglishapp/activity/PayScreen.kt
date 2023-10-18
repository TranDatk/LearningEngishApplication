package com.tnmd.learningenglishapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.tnmd.learningenglishapp.screens.chat.ChannelListViewModal
import com.tnmd.learningenglishapp.screens.sign_up.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import vn.momo.momo_partner.AppMoMoLib

@AndroidEntryPoint
@ExperimentalMaterialApi
class PayScreen() : AppCompatActivity() {
    private var isPaymentConfirmed by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
            PayScreenContent(isPaymentConfirmed) { orderId ->
                requestPayment(orderId)
                isPaymentConfirmed = true
            }
        }
    }

    @Composable
    fun PayScreenContent(
        isPaymentConfirmed: Boolean,
        onConfirmPayment: (String) -> Unit
    ) {
        if (isPaymentConfirmed) {
            // Display a notification when payment is confirmed
            Text("Payment confirmed!")
        } else {
            // Display the payment confirmation button
            Button(
                onClick = {
                    // Perform payment confirmation and call the callback function
                    val orderId = "orderId123" // Replace orderId123 with the appropriate value
                    onConfirmPayment(orderId)
                }
            ) {
                Text("Confirm Payment")
            }
        }
    }

    private val amount = "10000"
    private val fee = "0"
    private val environment = 0 // developer default

    private val merchantName = "Payment Order"
    private val merchantCode = "MOMOO41P20220925"

    private val merchantNameLabel = "HotelRent"
    private val description = "Top up account"

    private fun requestPayment(orderId: String) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
        val eventValue = HashMap<String, Any>()
        // client Required
        eventValue["merchantname"] = merchantName
        eventValue["merchantcode"] = merchantCode
        eventValue["amount"] = amount
        eventValue["orderId"] = orderId
        eventValue["orderLabel"] = orderId

        // client Optional - bill info
        eventValue["merchantnamelabel"] = merchantNameLabel
        eventValue["fee"] = fee
        eventValue["description"] = description

        // client extra data
        eventValue["requestId"] = "$merchantCode merchant_billId_${System.currentTimeMillis()}"
        eventValue["partnerCode"] = merchantCode

        // Example extra data
        val objExtraData = JSONObject()
        try {
            objExtraData.put("site_code", "008")
            objExtraData.put("site_name", "CGV Cresent Mall")
            objExtraData.put("screen_code", 0)
            objExtraData.put("screen_name", "Special")
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3")
            objExtraData.put("movie_format", "2D")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        eventValue["extraData"] = objExtraData.toString()

        eventValue["extra"] = ""
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            data?.let {
                when (it.getIntExtra("status", -1)) {
                    0 -> {
                        // TOKEN IS AVAILABLE
                        val token = it.getStringExtra("data") // Token response
                        val phoneNumber = it.getStringExtra("phonenumber")
                        Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
                        var env = it.getStringExtra("env")
                        if (env == null) {
                            env = "app"
                        }
                        if (token != null && token != "") {
                            // Handle successful payment
                        } else {
                            Log.d("thanhcong", "Payment failed")
                        }
                    }

                    1, 2 -> {
                        // TOKEN FAIL
                        val message = it.getStringExtra("message") ?: "Failed"
                        Log.d("thanhcong", "Payment failed")
                    }

                    else -> {
                        // TOKEN FAIL
                        Log.d("thanhcong", "Payment failed")
                    }
                }
            } ?: run {
                Log.d("thanhcong", "Payment failed")
            }
        } else {
            Log.d("thanhcong", "Payment failed")
        }
    }
}

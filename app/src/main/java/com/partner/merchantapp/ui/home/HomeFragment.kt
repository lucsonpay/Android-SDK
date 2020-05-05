package com.partner.merchantapp.ui.home

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.partner.merchantapp.R
import com.partner.merchantapp.R.layout.fragment_home
import com.partner.paymentgateway.sdk.PaySdkInt
import com.partner.paymentgateway.util.AppUtil
import com.partner.paymentgateway.util.MerchantParams
import com.partner.paymentgateway.util.PayReqParams
import com.partner.paymentgateway.util.PayResParams
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.NonCancellable.cancel


class HomeFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var pay : Button
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(fragment_home, container, false)
       // val textView: TextView = root.findViewById(R.id.text_home)
        pay  = root.findViewById(R.id.pay)
        pay.setOnClickListener(this)

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
         //   textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkBox.setOnCheckedChangeListener(this)
        checkBox.isChecked = true
        checkBox.isChecked = false
        edAmount.setSelection(0)
    }
    override fun onClick(v: View?) {
        if(!edAmount.text.toString().isBlank()) {
            getPay(edAmount.text.toString())
        }else{
            Toast.makeText(context,"Enter Amount",Toast.LENGTH_LONG).show()
        }
        if(isInputEmpty(merName))
            return
        if(isInputEmpty(custName))
            return
        if(isInputEmpty(merPayId))
            return
        if(isInputEmpty(merKey))
            return
        if(isInputEmpty(uriRequest))
            return
        if(isInputEmpty(uriResponse))
            return
    }

    private fun isInputEmpty(edit: TextInputEditText?) :Boolean{
        if(edit?.text.toString().isBlank()){
            Toast.makeText(context,"${edit?.hint} can not be blank.",Toast.LENGTH_LONG).show()
           return true
        }
        return false
    }

    fun getPay(amount: String?) {
        try {
            var merchantParams = MerchantParams()
            merchantParams.merAmount = (Integer.valueOf(amount!!) * 100).toString()
            merchantParams.merCurrencyCode = "356"
            merchantParams.merCustAddress = "Gurgaon"
            merchantParams.merCustEmail = "neeeeeraj.kumar@pay.com"
            merchantParams.merCustPhone = "9999999999"
            merchantParams.merCustZip = "122016"
            merchantParams.merName = merName.text.toString().trim()
            merchantParams.merCustName= custName.text.toString().trim()
            merchantParams.merPayId =merPayId.text.toString().trim()
            merchantParams.merKey = merKey.text.toString().trim()
            merchantParams.requestUrl = uriRequest.text.toString().trim()
            merchantParams.responseUrl = uriResponse.text.toString().trim()

            if (checkBox.isChecked()) {
//                merchantParams.merName = "BHARTIPAY Live"
//                merchantParams.merCustName= "BHARTIPAY LIVE"
//                merchantParams.merPayId = "1804071319261021" //1804071319261021//test 1806050627121026
//                merchantParams.merKey = "6275179d6d2a4ad5" //6275179d6d2a4ad5//test 4964b2d80d114aed
     //           merchantParams.requestUrl = "https://merchant.bhartipay.com/crm/jsp/paymentrequest"
   //             merchantParams.responseUrl = "https://merchant.bhartipay.com/crm/jsp/sdkResponse.jsp"
            } else {
               /* merchantParams.merName = "BHARTIPAY Uat"
                merchantParams.merCustName = "BHARTIPAY UAT"
                merchantParams.merPayId = "2001141020561000" //1804071319261021//test 1806050627121026
                merchantParams.merKey = "8535b0d335e545d4"//6275179d6d2a4ad5//test 4964b2d80d114aed*/
       //         merchantParams.requestUrl = "https://uat.bhartipay.com/crm/jsp/paymentrequest"
       //         merchantParams.responseUrl = "https://uat.bhartipay.com/crm/jsp/sdkResponse.jsp"
            }
            merchantParams.merOrderId= System.currentTimeMillis().toString()
            merchantParams.merProductDesc = "Demo Transaction"
            merchantParams.merTxnType = "SALE"
            merchantParams.actionBarTitle = "Payment Gateway"
            merchantParams.isProductionEnv = checkBox.isChecked()
            merchantParams.actionBarColor = R.color.colorAccent
            val params = PayReqParams()
            params.setMerchantParams(merchantParams)
            print("#######$params")
            PaySdkInt.getInstance(this, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == AppUtil.PG_REQ_CODE && resultCode == RESULT_OK) {
            var resParams: PayResParams? = null
            if (data != null && data.extras != null && data.getParcelableExtra<Parcelable?>(
                    AppUtil.PG_RESULT
                ) != null
            ) {
                resParams =
                    data.getParcelableExtra<Parcelable>(AppUtil.PG_RESULT) as PayResParams
                if (resParams != null) {
                    /*val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
                    dialog.setMessage(
                        "Status     :>" + resParams.STATUS  
                            .toString() + "\n" + "TxnId      :>" + resParams.TXN_ID
                            .toString() + "\n" + "Date :>" + resParams.RESPONSE_DATE_TIME
                            .toString() + "\n" + "Order ID  :>" + resParams.ORDER_ID
                            .toString() + "\n" + "ResponseMsg    :>" + resParams.RESPONSE_MESSAGE
                            .toString() + "\n" + "ResponseCode:>" + resParams.RESPONSE_CODE
                            .toString() + "\n" + "Amount (in paisa):>" + resParams.AMOUNT
                    )
                    dialog.show()*/
                    MaterialAlertDialogBuilder(context)
                        .setTitle(resources.getString(R.string.header))
                        .setMessage("Status     :>" + resParams.STATUS
                            .toString() + "\n" + "TxnId      :>" + resParams.TXN_ID
                            .toString() + "\n" + "Date :>" + resParams.RESPONSE_DATE_TIME
                            .toString() + "\n" + "Order ID  :>" + resParams.ORDER_ID
                            .toString() + "\n" + "ResponseMsg    :>" + resParams.RESPONSE_MESSAGE
                            .toString() + "\n" + "ResponseCode:>" + resParams.RESPONSE_CODE
                            .toString() + "\n" + "Amount (in paisa):>" + resParams.AMOUNT)
                        .setPositiveButton(resources.getString(R.string.cancel)) { dialog, which ->
                            // Respond to positive button press
                        }

                        .show()
                    println(requestCode.toString() + ":>>>>>>>>>>>>>.." + resultCode + ":>>>>>>>>>>>>.." + resParams.RESPONSE_CODE)
                }
            }
        } else if (requestCode == AppUtil.PG_REQ_CODE && resultCode == RESULT_CANCELED) {
            var resParams: PayResParams? = null
            if (data != null && data.extras != null) {
                val cancelCode = data.getIntExtra(AppUtil.PG_CANCEL_CODE, 0)
                resParams =
                    data.getParcelableExtra<Parcelable>(AppUtil.PG_RESULT) as PayResParams
                if (cancelCode == 1 && resParams != null) { //transaction failed from bank
                   /* val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
                    dialog.setMessage(
                        "ResponseMsg    :>" + data.getStringExtra(AppUtil.PG_MESSAGE)
                                + "\n" + "ResponseCode:>" + resParams.RESPONSE_CODE
                                + "\n" + "Amount (in paisa) :>" + resParams.AMOUNT
                    )
                    dialog.show()*/
                    MaterialAlertDialogBuilder(context)
                        .setTitle(resources.getString(R.string.header))
                        .setMessage("ResponseMsg    :>" + data.getStringExtra(AppUtil.PG_MESSAGE)
                                + "\n" + "ResponseCode:>" + resParams.RESPONSE_CODE
                                + "\n" + "Amount (in paisa) :>" + resParams.AMOUNT)
                        .setPositiveButton(resources.getString(R.string.cancel)) { dialog, which ->
                            // Respond to positive button press
                        }
                        .show()
                } else if (cancelCode == 2) { //transaction cancel by user
                    val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
                    dialog.setMessage("ResponseMsg    :>" + data.getStringExtra(AppUtil.PG_MESSAGE))
                    dialog.show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(isChecked) {
            true ->{
                merName.text  = Editable.Factory.getInstance().newEditable("BHARTIPAY Live")
                custName.text = Editable.Factory.getInstance().newEditable("BHARTIPAY LIVE")
                merPayId.text = Editable.Factory.getInstance().newEditable("1804071319261021")
                merKey.text = Editable.Factory.getInstance().newEditable("6275179d6d2a4ad5")
                test_card.text = Editable.Factory.getInstance().newEditable("")
                uriRequest.text = Editable.Factory.getInstance().newEditable("https://merchant.bhartipay.com/crm/jsp/paymentrequest")
                uriResponse.text = Editable.Factory.getInstance().newEditable("https://merchant.bhartipay.com/crm/jsp/sdkResponse.jsp")
                edAmount.setSelection(0)

            }
            false ->{
                merName.text  = Editable.Factory.getInstance().newEditable("BHARTIPAY Uat")
                custName.text = Editable.Factory.getInstance().newEditable("BHARTIPAY UAT")
                merPayId.text = Editable.Factory.getInstance().newEditable("2001141020561000")
                merKey.text = Editable.Factory.getInstance().newEditable("8535b0d335e545d4")
                test_card.text = Editable.Factory.getInstance().newEditable("4000000000000002")
                uriRequest.text = Editable.Factory.getInstance().newEditable("https://uat.bhartipay.com/crm/jsp/paymentrequest")
                uriResponse.text = Editable.Factory.getInstance().newEditable("https://uat.bhartipay.com/crm/jsp/sdkResponse.jsp")
                edAmount.setSelection(0)
            }
        }
    }

}

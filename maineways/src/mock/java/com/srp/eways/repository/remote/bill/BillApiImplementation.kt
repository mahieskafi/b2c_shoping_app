package com.srp.eways.repository.remote.bill

import android.os.Handler
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.BillPaymentRequest
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.BillPaymentResult
import com.srp.eways.model.bill.BillPaymentStatusResult
import com.srp.eways.model.bill.archivedList.BillTemp
import com.srp.eways.model.bill.archivedList.BillTempResponse
import com.srp.eways.model.deposit.Bank
import com.srp.eways.model.deposit.BankListResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by ErfanG on 5/3/2020.
 */
object BillApiImplementation : BaseApiImplementation(), BillApiService {


    override fun saveTempBills(billRequest: BillPaymentRequest, callBackWrapper: CallBackWrapper<BillPaymentResponse>) {
        setMode(NetworkResponseCodes.SUCCESS)

        if (!handleCall(callBackWrapper)) {
            Handler().postDelayed({
                callBackWrapper.onSuccess(getSaveResponse(billRequest))
            }
                    , getResponseDelay())
        }
    }

    private fun getSaveResponse(billRequest: BillPaymentRequest): BillPaymentResponse {

        var billTempList: ArrayList<BillTemp>? = getBillResponse(1).data!!
        billTempList!!.add(BillTemp(id = 1000, billId = billRequest.bills[0].billId, billTypeId = (1..8).random(), paymentId = billRequest.bills[0].payId, logDate = "2020-04-25T07:47:27.852Z", price = 450000, description = ""))

        var billPaymentResult = BillPaymentResult(bills = billTempList)
        return BillPaymentResponse(status = 7, description = "", result = billPaymentResult)
    }

    private fun getBillResponse(pageIndex: Int): BillTempResponse {
        var billList = ArrayList<BillTemp>()

        for (i in pageIndex..pageIndex + 3) {
            var billTemp = BillTemp(id = i, billId = "425345645", billTypeId = (1..8).random(), paymentId = "425345645", logDate = "2020-04-25T07:47:27.852Z", price = 450000, description = "")

            billList.add(billTemp)
        }

        return BillTempResponse(status = 0, description = "", data = billList)
    }


    override fun payBills(billRequest: BillPaymentRequest, callBackWrapper: CallBackWrapper<BillPaymentResponse>) {
        if (handleCall(callBackWrapper))
            return
        Handler().postDelayed({
            callBackWrapper.onSuccess(createSuccessPaymentResponse(billRequest))
        }, getResponseDelay())
    }

    private fun createSuccessPaymentResponse(billRequest: BillPaymentRequest): BillPaymentResponse {

        var billTempList = arrayListOf<BillTemp>()
        billTempList.add(BillTemp(id = 1000, billId = billRequest.bills[0].billId, billTypeId = (1..8).random(), paymentId = billRequest.bills[0].payId, logDate = "2020-04-25T07:47:27.852Z", price = 450000, description = "", status = 5))

        var billPaymentResult = BillPaymentResult(bills = billTempList)
        return BillPaymentResponse(status = 5, description = "", result = billPaymentResult)
    }

    override fun getBankList(callBack: CallBackWrapper<BankListResponse>) {
        if (handleCall(callBack))
            return
        Handler().postDelayed({
            callBack.onSuccess(createSuccessResultBankList())
        }, getResponseDelay())
    }

    private fun createSuccessResultBankList(): BankListResponse {
        var bankList: ArrayList<Bank> = ArrayList()
        bankList.add(Bank(gId = 41, pName = "از سپرده کسر شود", eName = "Parsian Bill Credit", bankType = 4))
        bankList.add(Bank(gId = 42, pName = "پارسیان", eName = "mellat", bankType = 4))
        bankList.add(Bank(gId = 44, pName = "سامان", eName = "jiring", bankType = 4))

        return BankListResponse(status = NetworkResponseCodes.SUCCESS, items = bankList)
    }

    override fun getPaymentStatus(requestId: String, callBack: CallBackWrapper<BillPaymentStatusResult>) {

    }
}
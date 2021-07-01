package com.srp.eways.ui.bill.payment

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.srp.eways.R
import com.srp.eways.base.BaseActivity
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.util.BillUtil
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.*

class BillPaymentQrActivity : BaseActivity<BillPaymentViewModel>(), ZXingScannerView.ResultHandler {

    private lateinit var mToolbar: WeiredToolbar
    private lateinit var mScannerView: ZXingScannerView
    private var mFlash = false
    private var mSelectedIndices: ArrayList<Int>? = null

    companion object {

        private const val FLASH_STATE = "FLASH_STATE"
        private const val SELECTED_FORMATS = "SELECTED_FORMATS"

        fun newIntent(context: Context): Intent {
            return Intent(context, BillPaymentQrActivity::class.java)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_qr_scanner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mToolbar = findViewById(R.id.toolbar)
        val flashIcon: AppCompatImageView = findViewById(R.id.flasher)

        flashIcon.setOnClickListener()
        {
            mFlash = !mFlash
            mScannerView.flash = mFlash
        }

        setupToolbar()

        val contentFrame: ViewGroup = findViewById(R.id.content_frame)
        mScannerView = ZXingScannerView(this)
        contentFrame.addView(mScannerView)

        setupFormats()
    }

    fun setupToolbar() {
        val resources = DIMain.getABResources()

        mToolbar.setShowTitle(true)
        mToolbar.setShowDeposit(false)
        mToolbar.setShowShop(false)
        mToolbar.setShowNavigationDrawerMenu(false)
        mToolbar.setShowNavigationUp(true)
        mToolbar.setTitle(resources.getString(R.string.bill_payment_qr_page_title))
//        mToolbar.setTitleIcon(resources.getDrawable(R.drawable.ic_qr_payment))
        mToolbar.setBackgroundColor(resources.getColor(R.color.buycharge_toolbar_background))
        mToolbar.setOnBackClickListener() { onBackPressed() }
    }

    private fun setupFormats() {
        val formats: MutableList<BarcodeFormat> = ArrayList()
        if (mSelectedIndices == null || mSelectedIndices!!.isEmpty()) {
            mSelectedIndices = ArrayList<Int>()
            for (i in ZXingScannerView.ALL_FORMATS.indices) {
                mSelectedIndices!!.add(i)
            }
        }
        for (index in mSelectedIndices!!) {
            formats.add(ZXingScannerView.ALL_FORMATS[index])
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats)
        }
    }

    override fun handleResult(rawResult: Result?) {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(this, notification)
            r.play()
        } catch (e: Exception) {
        }

        var billDetail = BillUtil.getPaymentDetailFromBarcode(rawResult!!.text)
        if (billDetail == null) {
            Toast.makeText(this, getString(R.string.bill_payment_qr_error), Toast.LENGTH_LONG).show()
        } else {
            mScannerView.stopCamera()
            viewModel.setBillPaymentDetail(BillUtil.getPaymentDetailFromBarcode(rawResult!!.text))
            onBackPressed()
        }
        mScannerView.resumeCameraPreview(this)
    }


    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }
    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun getViewModel(): BillPaymentViewModel {
        return DIMain.getViewModel(BillPaymentViewModel.getInstance().javaClass)
    }
}
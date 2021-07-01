package com.srp.eways.ui.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import com.srp.eways.R
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.view.LoadingStateView
import com.srp.eways.util.Constants
import com.srp.eways.util.Utils


open class MainWebFragment(url: String) : NavigationMemberFragment<BaseViewModel>() {
    override fun acquireViewModel(): BaseViewModel? {
        return null
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_web
    }

    companion object {
        fun newInstance(url: String): MainWebFragment {
            return MainWebFragment(url)
        }
    }

    private var mUrl = url
    private lateinit var mLoadingStateView: LoadingStateView
    private lateinit var mWebView: WebView
    private lateinit var mUrlView: TextView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mWebView = view.findViewById(R.id.webview)
        mLoadingStateView = view.findViewById(R.id.loadingstateview)
        mUrlView = view.findViewById(R.id.show_url)
        setupLoadingStateView()

        view.isFocusableInTouchMode = true
        view.requestFocus()
        mWebView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    super.onBackPressed()
                    return@OnKeyListener true
                }
            }
            false
        })



        mWebView.settings.javaScriptEnabled = true

        mWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView?, errorCode: Int, description: String, failingUrl: String?) {
                if (context != null) {
                    Toast.makeText(context, description, Toast.LENGTH_SHORT).show()
                }
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView?, req: WebResourceRequest, rerr: WebResourceError) { // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.errorCode, rerr.description.toString(), req.url.toString())
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val intent: Intent
                mUrlView.text = url

                if (url?.contains(Constants.DEEPLINK_SCHEME, true)!!) {
                    if (url.contains(Constants.DEEPLINK_BILL, true)) {

                        val pathSegments = Uri.parse(url).pathSegments
                        if (pathSegments.size >= 3) {
                            val requestId = pathSegments[1]
                            if (pathSegments[0].toInt() == 8) {
                                val billPaymentTypeViewModel = DIMain.getViewModel(BillPaymentTypeViewModel.getInstance().javaClass)

                                billPaymentTypeViewModel?.deepLinkResponseReceived(requestId)
                            }
                        }

                        onBackPressed()
                        return true
                    } else if (url.contains(Constants.DEEPLINK_CREDIT, true)) {
                        val pathSegments = Uri.parse(url).pathSegments
                        if (pathSegments.size >= 3) {
                            val uuid = pathSegments[1]
                            val uid = pathSegments[2]
                            if (pathSegments[0].toInt() == 8) {
                                val mDepositViewModel: IncreaseDepositViewModel = DIMain.getViewModel(IncreaseDepositViewModel::class.java)
                                mDepositViewModel.getStatus(uid, uuid)
                            }
                        }
                        onBackPressed()
                        return true
                    } else if (url.contains(Constants.DEEPLINK_STORE, true)) {
//                        onSwitchRoot(MainRootIds.ROOT_HOME)
//                        onBackPressed()
                        handleStore(Uri.parse(url))
                        return true
                    }
                }
                return false
            }
        }

        if (Utils.isInternetAvailable()) {
            mWebView.loadUrl(mUrl)
        } else {
            mWebView.visibility = View.GONE
            mLoadingStateView.visibility = View.VISIBLE
        }
    }

    private fun setupLoadingStateView() {
        var abResources = DIMain.getABResources()

        mLoadingStateView.visibility = View.GONE
        mLoadingStateView.setButtonText(abResources.getString(R.string.survey_end_page_button_text))
        mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, abResources.getString(R.string.network_error_no_internet), true)
        mLoadingStateView.setRetryListener {
            onBackPressed()
        }
    }

    open fun handleStore(url: Uri) {

    }

    override fun getDataFromServer() {
        mWebView.visibility = View.VISIBLE
        mLoadingStateView.visibility = View.GONE

        mWebView.loadUrl(mUrl)
    }

    override fun onInternetUnAvailable() {
        mWebView.visibility = View.GONE
        mLoadingStateView.visibility = View.VISIBLE
    }
}
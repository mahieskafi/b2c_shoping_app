package com.srp.b2b2cEwaysPannel.ui.navigationdrawer

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.srp.b2b2cEwaysPannel.di.DI
import com.srp.b2b2cEwaysPannel.ui.login.LoginActivity
import com.srp.eways.ui.charge.ChargeViewModel
import com.srp.eways.ui.navigationdrawer.MainNavigationDrawerFragment
import com.srp.eways.util.Constants

class NavigationDrawerFragment : MainNavigationDrawerFragment() {
    override fun getIncomeVisibility(): Int {
        return View.GONE
    }

    override fun getSaleReportVisibility(): Int {
        return View.GONE
    }

    override fun getSupportVisibility(): Int {
        return View.VISIBLE
    }

    override fun getAboutUsVisibility(): Int {
        return View.VISIBLE
    }

    override fun getPointsVisibility(): Int {
        return View.GONE
    }

    override fun logoutAction() {
        viewModel.logout()

        viewModel.getIsSuccessLiveData().observe(this, Observer { isSuccess ->
            if (isSuccess != null) {
                if (isSuccess) {
                    DI.getPreferenceCache().put(Constants.USER_INFO, "")
                    DI.getPreferenceCache().put(Constants.TOKEN_KEY, "")

                    getUserInfoViewModel().consumeUserInfoLiveData()
                    getUserInfoViewModel().consumeCreditLiveData()
                    getUserInfoViewModel().invalidateCredit()

                    DI.getViewModel(ChargeViewModel::class.java).makeInstanceNull()

                    activity?.finish()
                    startActivity(LoginActivity.newIntent(context))
                }
            }
        })

    }
}
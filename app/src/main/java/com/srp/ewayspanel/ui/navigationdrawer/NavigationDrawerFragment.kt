package com.srp.ewayspanel.ui.navigationdrawer

import android.view.View
import com.srp.eways.ui.charge.ChargeViewModel
import com.srp.eways.ui.navigationdrawer.MainNavigationDrawerFragment
import com.srp.eways.util.Constants
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.ui.login.LoginActivity

class NavigationDrawerFragment : MainNavigationDrawerFragment() {
    override fun getIncomeVisibility(): Int {
        return View.VISIBLE
    }

    override fun getSaleReportVisibility(): Int {
        return View.VISIBLE
    }

    override fun getSupportVisibility(): Int {
        return View.VISIBLE
    }

    override fun getAboutUsVisibility(): Int {
        return View.VISIBLE
    }

    override fun getPointsVisibility(): Int {
        return View.VISIBLE
    }

    override fun logoutAction() {
        viewModel.logout()

        DI.getPreferenceCache().put(Constants.USER_INFO, "")
        DI.getPreferenceCache().put(Constants.TOKEN_KEY, "")

        getUserInfoViewModel().consumeUserInfoLiveData()
        getUserInfoViewModel().invalidateCredit()

        DI.getViewModel(ChargeViewModel::class.java).makeInstanceNull()

        startActivity(activity?.baseContext?.let { it1 -> LoginActivity.newIntent(it1) })
        activity?.finish()
    }

}
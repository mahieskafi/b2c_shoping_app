package com.srp.eways.ui.navigationdrawer

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.model.logout.LogoutResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.logout.LogoutRepository
import com.srp.eways.util.Constants

/**
 * Created by ErfanG on 27/08/2019.
 */
class NavigationDrawerViewModel : BaseViewModel() {

    private val mLogoutRepository: LogoutRepository
    private val mIsSuccessLive: MutableLiveData<Boolean>

    init {
        mLogoutRepository = DIMain.getLogoutRepo()
        mIsSuccessLive = MutableLiveData()
    }

    fun logout() {
        mLogoutRepository.logout(object : CallBackWrapper<LogoutResponse> {
            override fun onError(errorCode: Int, errorMessage: String?) {
                mIsSuccessLive.value = false
            }

            override fun onSuccess(responseBody: LogoutResponse?) {
                if (responseBody?.status == 0) {
                    mIsSuccessLive.value = true
                }
            }
        })
    }

    fun getIsSuccessLiveData(): MutableLiveData<Boolean> {
        return mIsSuccessLive
    }
}
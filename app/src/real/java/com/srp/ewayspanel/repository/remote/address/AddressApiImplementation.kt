package com.srp.ewayspanel.repository.remote.address

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback
import com.srp.ewayspanel.model.address.CityResponse
import com.srp.ewayspanel.model.address.ProvinceResponse

open class AddressApiImplementation : BaseApiImplementation(), AddressApiService {

    private val mAddressApiRetrofit: AddressApiRetrofit

    companion object {
        val instance = AddressApiImplementation()
    }

    init {
        mAddressApiRetrofit = DIMain.provideApi(AddressApiRetrofit::class.java)
    }

    override fun getProvinces(callback: CallBackWrapper<ProvinceResponse>) {
        mAddressApiRetrofit.getProvinces(AppConfig.SERVER_VERSION).enqueue(object : DefaultRetroCallback<ProvinceResponse>(callback) {

            override fun checkResponseForError(response: ProvinceResponse, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

    override fun getCities(provinceId: Int, callback: CallBackWrapper<CityResponse>) {
        mAddressApiRetrofit.getCities(AppConfig.SERVER_VERSION,provinceId).enqueue(object : DefaultRetroCallback<CityResponse>(callback) {

            override fun checkResponseForError(response: CityResponse, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

}
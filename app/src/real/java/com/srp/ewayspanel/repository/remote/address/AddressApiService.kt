package com.srp.ewayspanel.repository.remote.address

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.address.CityResponse
import com.srp.ewayspanel.model.address.ProvinceResponse

interface AddressApiService {

    fun getProvinces(callback: CallBackWrapper<ProvinceResponse>)

    fun getCities(provinceId: Int, callback: CallBackWrapper<CityResponse>)
}
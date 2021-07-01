package com.srp.eways.repository.remote.login

import com.srp.eways.model.login.LoginRequest
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.network.CallBackWrapper

interface MainLoginApiService {
    fun login(body: LoginRequest, callBack : CallBackWrapper<LoginResponse>)
}
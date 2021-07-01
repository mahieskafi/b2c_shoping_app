package com.srp.eways.repository.remote.validatetoken

import android.os.Handler
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.model.login.UserInfo
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by Eskafi on 8/25/2019.
 */
class ValidateTokenApiImplementation : BaseApiImplementation(), ValidateTokenApiService {
    

    companion object {

        private var instance: ValidateTokenApiImplementation? = null

        fun getInstance(): ValidateTokenApiImplementation {

            if (instance == null) {
                instance = ValidateTokenApiImplementation()
            }
            return instance as ValidateTokenApiImplementation
        }
    }


    override fun validateToken(callBack: CallBackWrapper<LoginResponse>) {
//        setMode(SUCCESS)
        if (!handleCall(callBack)) {
            Handler().postDelayed({
                callBack.onSuccess(createSuccessResult())
            }, getResponseDelay())
        }

    }

    private fun createSuccessResult(): LoginResponse {
        val userInfo = UserInfo(credit = 50000)
        val response = LoginResponse("Successful", NetworkResponseCodes.SUCCESS, "sasdasdc", userInfo)
        return response
    }

}

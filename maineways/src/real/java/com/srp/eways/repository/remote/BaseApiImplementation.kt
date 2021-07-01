package com.srp.eways.repository.remote


import retrofit2.Response

/**
 * Created by ErfanG on 07/08/2019.
 */
open public abstract class BaseApiImplementation {

    fun <T>hasImplementation(response: Response<T>): Boolean {

        return response.isSuccessful
    }

    fun <T>getImplementation(response: Response<T>): T {

        TODO("newInstance implemenetation")
    }
}
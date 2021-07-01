package com.srp.eways.model.login

/**
 * Created by ErfanG on 8/7/2019.
 */
data class LoginRequest(
    val AppKey: String?,
    val Info: String?,
    val Password: String?,
    val RememberMe: Boolean?,
    val Type: Int?,
    val UserName: String?,
    val TraceCode: String?
)
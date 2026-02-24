package com.example.moviesandmore.core.network

import retrofit2.Response

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        } else {
            ApiResult.Error(response.message(), response.code())
        }
    } catch (e: Exception) {
        ApiResult.Exception(e)
    }
}

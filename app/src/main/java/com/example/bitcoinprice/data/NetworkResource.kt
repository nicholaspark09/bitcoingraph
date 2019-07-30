package com.example.bitcoinprice.data

import androidx.annotation.StringRes
import androidx.lifecycle.Observer

sealed class NetworkResource<T> {
    data class Success<T>(val data: T? = null) : NetworkResource<T>()

    data class Error<T>(val data: T? = null,
                        val exception: Throwable? = null,
                        val errorCode: Int? = 0,
                        val errorMessage: String? = null,
                        @StringRes val localizedMessage: Int? = null) : NetworkResource<T>()

    class Loading<T> : NetworkResource<T>()
}

class NetworkResourceObserver<T>(private val onSuccess: (T?) -> Unit = {},
                                 private val onFailure: (exception: Throwable?, errorCode: Int?, errorMessage: String?) -> Unit = { _, _, _ -> },
                                 private val onLoading: () -> Unit = {}) : Observer<NetworkResource<T>> {

    override fun onChanged(resource: NetworkResource<T>?) {
        when (resource) {
            is NetworkResource.Success -> onSuccess(resource.data)
            is NetworkResource.Error -> onFailure(resource.exception, resource.errorCode, resource.errorMessage)
            is NetworkResource.Loading -> onLoading()
        }
    }
}
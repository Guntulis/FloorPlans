package com.example.currencyapp.data.api

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    class Empty<T> : Resource<T>()
    class Complete<T>(val value: T) : Resource<T>()
    class Error<T>(val error: String) : Resource<T>()
}

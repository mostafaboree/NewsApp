package com.example.newsapi.data.model

sealed class Result<out T>() {
    data class Success<out T>(val data :T ) : Result<T>()
    data class Error(val message : String,val errorCode : Int) : Result<Nothing>()
    data object Loading : Result<Nothing>()

}
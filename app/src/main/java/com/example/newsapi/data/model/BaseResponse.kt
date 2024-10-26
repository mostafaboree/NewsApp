package com.example.newsapi.data.model

 data class BaseResponse<T>(
     val status: String = "success",
     val data: T? = null,
 )

package com.example.newsapi.data.remote

import com.example.newsapi.data.model.BaseResponse
import com.example.newsapi.data.model.Category
import com.example.newsapi.data.model.NewsResponse
import com.example.newsapi.data.model.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApiService {
    @GET("top-headlines/category/{category}/us.json")
    suspend fun getTopHeadlines(@Path("category") category: String): Response<NewsResponse>
    @GET("sources.json")
    suspend fun getSource(): Response<SourceResponse>

    //https://saurav.tech/NewsAPI/top-headlines/category/health/in.json

}
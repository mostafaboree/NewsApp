package com.example.newsapi.data.remote

import com.example.newsapi.data.model.CategoriesResponse
import com.example.newsapi.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines/category/{category}/us.json")
    suspend fun getTopHeadlines(@Path("category") category: String): Response<NewsResponse>

    //https://saurav.tech/NewsAPI/top-headlines/category/health/in.json

}
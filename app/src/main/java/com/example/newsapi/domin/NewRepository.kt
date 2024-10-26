package com.example.newsapi.domin

import com.example.newsapi.data.Local.ArticleEntity
import com.example.newsapi.data.model.Category
import com.example.newsapi.data.model.NewsResponse
import com.example.newsapi.data.model.Result
import retrofit2.Response

interface NewsRepository {
        suspend fun getTopHeadlines( category: String): Response<NewsResponse>
        suspend fun saveLastArtical(list: List<ArticleEntity>)
        suspend fun getSavedArticles(): List<ArticleEntity>
        suspend fun getSource(): Result<List<Category>>
    }


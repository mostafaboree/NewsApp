package com.example.newsapi.domin

import com.example.newsapi.data.Local.ArticleEntity
import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.CategoriesResponse
import com.example.newsapi.data.model.NewsResponse
import retrofit2.Response

interface NewsRepository {
        suspend fun getTopHeadlines( category: String): Response<NewsResponse>
        suspend fun saveLastArtical(list: List<ArticleEntity>)
        suspend fun getSavedArticles(): List<ArticleEntity>
    }


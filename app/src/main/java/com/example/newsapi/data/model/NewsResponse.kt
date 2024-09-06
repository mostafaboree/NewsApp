package com.example.newsapi.data.model

import com.example.newsapi.data.model.Article

data class NewsResponse(
        val status: String,
        val totalResults: Int,
        val articles: List<Article>

 )
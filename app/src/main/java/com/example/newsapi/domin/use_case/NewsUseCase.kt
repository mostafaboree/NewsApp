package com.example.newsapi.domin.use_case

import com.example.newsapi.data.Local.ArticleEntity
import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.Category
import com.example.newsapi.domin.NewsRepository
import javax.inject.Inject

// Domain Layer



interface GetArticlesUseCase {
    suspend fun getArticles(category: String): Result<List<Article>>
    suspend fun getOfflineArticles(): Result<List<ArticleEntity>>
    suspend fun saveNews(list: List<ArticleEntity>)
    suspend fun getSources(): com.example.newsapi.data.model.Result<List<Category>>
}

class GetArticlesUseCaseImpl @Inject constructor(
    private val repository: NewsRepository 
) :  GetArticlesUseCase {
    override suspend fun getArticles(category: String): Result<List<Article>> {
        return try {
            val response = repository.getTopHeadlines(category)
            if (response.isSuccessful) {
                Result.success(response.body()?.articles ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load articles"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOfflineArticles(): Result<List<ArticleEntity>> {
       return try {
           val response = repository.getSavedArticles()
           if (response.isNotEmpty()) {
               Result.success(response)
           } else {
               Result.failure(Exception("Failed to load articles"))
       }
    }catch (e: Exception) {
        Result.failure(e)
    }
}

    override suspend fun saveNews(list: List<ArticleEntity>) {
      repository.saveLastArtical(list)
    }

    override suspend fun getSources(): com.example.newsapi.data.model.Result<List<Category>> {
        return repository.getSource()

           }
}

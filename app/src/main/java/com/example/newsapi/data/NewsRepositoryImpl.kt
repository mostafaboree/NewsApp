package com.example.newsapi.data

import android.util.Log
import com.example.newsapi.data.Local.ArticleEntity
import com.example.newsapi.data.Local.NewsDao
import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.CategoriesResponse
import com.example.newsapi.data.model.NewsResponse
import com.example.newsapi.data.remote.NewsApiService
import com.example.newsapi.domin.NewsRepository
import com.example.newsapi.utls.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao,
    private val networkManager: NetworkMonitor
) : NewsRepository {

private var localNewsDao = listOf<ArticleEntity>()
    override suspend fun getTopHeadlines(
        category: String
    ): Response<NewsResponse> {
        return try {
            val response = apiService.getTopHeadlines(category)
            if (response.isSuccessful) {
               if (response.body() != null) {
                   localNewsDao = response.body()!!.articles.toArticleEntityList()
                   //Log.d("NewsRepositoryImpl", "Saving sss ${localNewsDao.size} articles")
               }
            }
            response
        } catch (e: Exception) {
            Response.error(500, ResponseBody.create(null, "Error fetching articles ${e.message}"))
        }
    }


    override suspend fun saveLastArtical(localNews: List<ArticleEntity>) {
           Log.d("NewsRepositoryImpl", "Saving in dao ${localNews.size} articles")
            newsDao.insertArticles(localNews)
        }


    override suspend fun getSavedArticles(): List<ArticleEntity> {
        Log.d("NewsRepositoryImpl", "Getting saved articles${newsDao.getArticles().size}")
         return newsDao.getArticles()
    }



}

 suspend fun List<Article>.toArticleEntityList(): List<ArticleEntity> =
    withContext(Dispatchers.Default){
     this@toArticleEntityList.map { it.toArticleEntity()
    }
}

suspend fun Article.toArticleEntity(): ArticleEntity = withContext(Dispatchers.Default) {
    ArticleEntity(
        key = 0,
        url = this@toArticleEntity.url,
        source = this@toArticleEntity.source,
        author = this@toArticleEntity.author,
        title = this@toArticleEntity.title,
        description = this@toArticleEntity.description,
        urlToImage = this@toArticleEntity.urlToImage,
        publishedAt = this@toArticleEntity.publishedAt ?: "",
        content = this@toArticleEntity.content

    )
}

package com.example.newsapi.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapi.data.Local.ArticleEntity
import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.Source
import com.example.newsapi.domin.use_case.GetArticlesUseCase
import com.example.newsapi.ui.NewsState.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<NewsState>(NewsState.Loading)
    val state: StateFlow<NewsState> get() = _state
    private val _Intent = MutableStateFlow<NewsIntent>(NewsIntent.SelectCategories("health"))
    private val _networkStatus = MutableStateFlow(false)
    val networkStatus: StateFlow<Boolean> get() = _networkStatus

    fun handleIntent(intent: NewsIntent) {
        viewModelScope.launch {
            _Intent.value = intent

            when (intent) {
                is NewsIntent.SelectCategories -> {
                    loadArticles(intent.category)
                }

                is NewsIntent.NavigationToDetails -> {
                    navigateToDetails(intent.article)
                }

                is NewsIntent.idel -> {
                    loadArticles("health")
                }

                is NewsIntent.LoadOfflineNews -> {

                }

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun loadArticles(category: String) {
        viewModelScope.launch {
            if (_networkStatus.value) {
                _state.value = NewsState.Loading
                val result = getArticlesUseCase.getArticles(category)
                if (result.isSuccess) {
                    _state.value = NewsState.ArticlesLoaded(result.getOrDefault(emptyList()))
                    var newarticle = ArrayList<ArticleEntity>()
                    result.getOrDefault(emptyList()).forEachIndexed() { index, item ->
                        newarticle.add(
                            ArticleEntity(
                                key = index + 1,
                                source =Source("${index+1}",item.source?.name!!),
                                url = item.url,
                                author = item.author,
                                urlToImage = item.urlToImage,
                                description = item.description,
                                title = item.title,
                                content = item.content,
                                publishedAt = item.publishedAt ?: ""
                            )
                        )
                    }
                    getArticlesUseCase.saveNews(newarticle)

                    Log.d(
                        ContentValues.TAG,
                        "loadArticles online : ${result.getOrDefault(emptyList())}"
                    )
                } else {
                    _state.value = Error(result.exceptionOrNull()?.message ?: "Unknown error")
                    Log.d(
                        ContentValues.TAG,
                        "loadArticles online error : ${result.getOrDefault(emptyList())}"
                    )

                }
            } else {
                val result = getArticlesUseCase.getOfflineArticles()

                var newarticle = ArrayList<Article>()
                result.getOrDefault(emptyList()).forEachIndexed() { index, item ->
                    newarticle.add(
                        Article(
                            source =Source("${index+1}",item.source?.name?:""),
                            url = item.url?:"",
                            author = item.author?:"",
                            urlToImage = item.urlToImage?:"",
                            description = item.description?:"",
                            title = item.title?:"",
                            content = item.content?:"",
                            publishedAt = item.publishedAt ?:""
                        )
                    )
                }
                _state.value = NewsState.ArticlesLoaded(newarticle)
                Log.d(ContentValues.TAG, "loadArticles offline: ${result}")

            }
        }
    }


    private fun navigateToDetails(article: Article) {
        _state.value = NewsState.ArticaleDetalise(article)
    }


    fun networkStatus(connected: Boolean) {
        _networkStatus.value = connected

    }


}


/**/
package com.example.newsapi.ui

import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.Category
sealed class NewsEvent() {
    object LoadOfflineNews : NewsEvent()
}
sealed class NewsOfflineState {
    object Loading : NewsOfflineState()
    data class ArticlesLoaded(val articles: List<Article>) : NewsOfflineState()
    data class Error(val message: String) : NewsOfflineState()
}


sealed class NewsIntent() {
    object LoadOfflineNews : NewsIntent()
    data class SelectCategories(val category: String) : NewsIntent()
    object idel : NewsIntent()
    data class NavigationToDetails(val article: Article) : NewsIntent()

}

sealed class NewsState() {
    object Loading : NewsState()
    data class ArticlesLoaded(val articles: List<Article>) : NewsState()
    data class ArticaleDetalise(val article: Article) : NewsState()
    data class Error(val message: String) : NewsState()


}
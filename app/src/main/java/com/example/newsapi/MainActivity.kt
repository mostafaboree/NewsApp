package com.example.newsapi

import android.annotation.SuppressLint
import android.content.ContentValues

import android.os.Bundle

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.Category
import com.example.newsapi.ui.Composable.BottomNavBar
import com.example.newsapi.ui.Composable.BottomTab
import com.example.newsapi.ui.NewsIntent
import com.example.newsapi.ui.NewsState
import com.example.newsapi.ui.NewsViewModel
import com.example.newsapi.ui.mainscreen.NewsApp
import com.example.newsapi.ui.theme.NewsApiTheme
import com.example.newsapi.utls.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    @SuppressLint("SuspiciousIndentation", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel = hiltViewModel<NewsViewModel>()
            val navController = rememberNavController()
            var isLoading by remember { mutableStateOf(true) }
            var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
            var selectedArticle by remember { mutableStateOf<Article?>(null) }
            var selectedCategory by remember { mutableStateOf("health") }
            var source by remember {
                mutableStateOf<List<Category>>(emptyList())
            }
            viewModel.handleIntent(NewsIntent.SelectCategories(selectedCategory))
            NewsApiTheme {

                Scaffold(modifier = Modifier.fillMaxSize().padding(top = 35.dp),
                    bottomBar = {
                        BottomNavBar(){
                            when (it) {
                                BottomTab.Home-> navController.navigate("home")
                                BottomTab.Source-> {
                                    viewModel.handleIntent(NewsIntent.LoadSources)
                                    navController.navigate("source")}
                                BottomTab.Bookmark-> navController.navigate("bookmark")
                            }
                        }

                    }
                ) { innerPadding ->
                    LaunchedEffect(viewModel) {
                        val networkMonitor = NetworkMonitor(this@MainActivity)
                        val isConnected = networkMonitor.isInternetAvailable()
                        Log.d(ContentValues.TAG, "What is the state $isConnected")
                        viewModel.networkStatus(isConnected)
                        if (isConnected) {
                        viewModel.handleIntent(NewsIntent.idel)
                        }
                        else{
                            isLoading = false
                            viewModel.handleIntent(NewsIntent.LoadOfflineNews)
                        }
                        viewModel.state.collect {
                            Log.d(ContentValues.TAG, "What is the state $it")
                            when (it) {
                                is NewsState.Loading -> {
                                    isLoading = true
                                }
                                is NewsState.ArticlesLoaded -> {
                                    isLoading = false
                                    articles = it.articles
                                }
                                is NewsState.Error -> {
                                    isLoading = false
                                }

                                is NewsState.ArticaleDetalise -> {
                                    selectedArticle = it.article
                                }
                                is NewsState.SourcesLoaded -> {
                                    source = it.categories
                                    Log.d(ContentValues.TAG, "cata  What is the state source ${source.size}")
                            }
                            }}}


                    NewsApp(
                        articles,
                        navController,
                        isLoading,
                        selectedArticle,
                        selectedCategory,
                        source,

                        {
                            viewModel.handleIntent(NewsIntent.NavigationToDetails(it))
                            navController.navigate("details")
                            selectedArticle = it
                        },
                        {
                           // viewModel.handleIntent(NewsIntent.SelectCategories(selectedCategory))
                            selectedCategory = it
                            viewModel.handleIntent(NewsIntent.SelectCategories(selectedCategory))
                        },
                        Modifier.padding(innerPadding),
                    )

                }
            }
        }
    }

}









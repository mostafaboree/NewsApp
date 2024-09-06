package com.example.newsapi.ui.mainscreen

import DetailsScreen
import WebViewScreen
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.Category
import com.example.newsapi.ui.NewsIntent
import com.example.newsapi.ui.NewsState
import com.example.newsapi.ui.NewsViewModel
import kotlinx.coroutines.delay

@Composable
fun NewsApp(
    articles: List<Article>,
    navController: NavHostController,
    isLoading: Boolean,
    article: Article?,
    category: String,
    selectedArticle: (article: Article) -> Unit,
    OnselectedCategory: (category: String) -> Unit,
    modifier: androidx.compose.ui.Modifier
) {

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController,
                isLoading,
                articles,
                category,

                intent = selectedArticle,
                onClick = OnselectedCategory
            )

        }
        composable("details") { backStackEntry ->
            //val articleUrl = backStackEntry.arguments?.getString("articleUrl") ?: ""
            DetailsScreen(article!!, navController)
        }

    }
}



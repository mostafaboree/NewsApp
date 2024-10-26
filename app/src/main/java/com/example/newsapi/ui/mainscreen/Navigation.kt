package com.example.newsapi.ui.mainscreen

import DetailsScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.Category
import com.example.newsapi.ui.BookMark.BookMarkScreen
import com.example.newsapi.ui.Composable.WebViewScr

import com.example.newsapi.ui.Source.SourceScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NewsApp(
    articles: List<Article>,
    navController: NavHostController,
    isLoading: Boolean,
    article: Article?,
    category: String,
    source:List<Category>,
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
            DetailsScreen(article!!, navController){title,url->
                navController.navigate("webview?url=${url}&title=${title}")
            }
        }
        composable("source") {
            SourceScreen(source,navController){url,title->
                val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                navController.navigate("webview?url=${encodedUrl}&title=${title}")
            }

        }
        composable("bookMark") {
            BookMarkScreen(navController,articles)
        }
        composable("webView?url={url}&title={title}",
            arguments = listOf(androidx.navigation.navArgument("url") {
                type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("title") {
                    type = androidx.navigation.NavType.StringType
                })) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
            val url = java.net.URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            val title = backStackEntry.arguments?.getString("title") ?: ""
            WebViewScr(navController, url,title){
                navController.popBackStack()
            }
Log.d("TAG", "NewsApp: $url")

        }


    }
}



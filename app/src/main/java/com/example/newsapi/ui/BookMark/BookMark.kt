package com.example.newsapi.ui.BookMark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.newsapi.data.model.Article
import com.example.newsapi.ui.mainscreen.NewsItem

@Composable
fun BookMarkScreen(navController: NavController,list: List<Article>){
    Box(modifier = Modifier.fillMaxSize())
    {
      LazyColumn(modifier = Modifier.fillMaxSize()){
          items(list){article ->
              NewsItem(article = article) {

              }
          }
      }

    }

}
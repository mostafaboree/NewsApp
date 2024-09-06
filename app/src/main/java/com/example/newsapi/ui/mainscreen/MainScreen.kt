package com.example.newsapi.ui.mainscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.booksshoe.presentation.ListScreen.LoadingAppScreen
import com.example.booksshoe.presentation.ListScreen.LoadingScreen
import com.example.newsapi.R
import com.example.newsapi.data.model.Article
import com.example.newsapi.ui.NewsIntent
import com.example.newsapi.ui.NewsState
import com.example.newsapi.ui.NewsViewModel
import com.valentinilk.shimmer.shimmer



val categoryList = listOf("health", "science", "sports", "technology", "entertainment", "general", "business")
@Composable
fun HomeScreen(navController: NavController,
               isloading:Boolean,
               articles:List<Article>,
               selectedCategory:String,
               intent: (article: Article) -> Unit,
               onClick: (category:String) -> Unit) {



    Column {
        LazyRow {
            items(categoryList) { category ->
                CategoryIte(
                    category = category,
                    onClick = {
                                 onClick(category)
                              },
                    isSelected = selectedCategory == category
                )
            }
        }
        Crossfade(targetState = isloading,
            animationSpec = tween(durationMillis = 1000)){state->
            if (!state) {
        LazyColumn {
            items(articles) { article ->
                NewsItem(article, onClick = {
                    intent(article)
                })
            }
        }
       }else{
        LoadingScreen()
    }
        }}}



@Composable
 private fun NewsItem(article: Article, onClick: (artical:Article) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick(
                    article
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            val painter = rememberAsyncImagePainter(
                model = article.urlToImage,
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                error = painterResource(id = R.drawable.ic_launcher_foreground)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                if (painter.state is AsyncImagePainter.State.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)
                            .shimmer()
                    )
                }
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun CategoryIte(
    category: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1.0f,
        animationSpec = tween(durationMillis = 300)
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.DarkGray else Color.LightGray,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .padding(8.dp)
            .scale(scale)
            .background(
                backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = category,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}




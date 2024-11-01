package com.example.newsapi.ui.mainscreen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.booksshoe.presentation.ListScreen.LoadingAppScreen
import com.example.booksshoe.presentation.ListScreen.LoadingScreen
import com.example.booksshoe.presentation.ListScreen.ShimmerloadingAnimation
import com.example.newsapi.R
import com.example.newsapi.data.model.Article
import com.example.newsapi.ui.NewsIntent
import com.example.newsapi.ui.NewsState
import com.example.newsapi.ui.NewsViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import java.util.Locale


val categoryList =
    listOf("health", "science", "sports", "technology", "entertainment", "general", "business")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isloading: Boolean,
    articles: List<Article>,
    selectedCategory: String,
    intent: (article: Article) -> Unit,
    onClick: (category: String) -> Unit


) {

    val trendingArticles = articles

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Headline Section
        Text(
            text = "Breaking News",
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
            fontWeight = FontWeight.Bold
        )





        if (articles.isNotEmpty()) {
            AnimatedBreakingNewsPager(
                articles = articles,
                onArticleClick = intent
            )
        }


        // Category Tabs
        LazyRow(
            modifier = Modifier.padding(8.dp)
        ) {
            items(categoryList) { category ->
                CategoryItem(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = { onClick(category) }
                )
            }
        }


        // Regular News Articles List
        Crossfade(
            targetState = isloading,
            animationSpec = tween(durationMillis = 1000)
        ) { state ->
            if (!state) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    items(articles) { article ->
                        NewsItem(article, onClick = { intent(article) })
                    }
                }
            } else {
                LoadingScreen()
            }
        }
    }
}
//

@Composable
fun BreakingNewsItem(article: Article, onClick: (article: Article) -> Unit) {
    val painter = rememberAsyncImagePainter(article.urlToImage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .clickable { onClick(article) }
            .shadow(12.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Increase height for a bigger image
                .clip(RoundedCornerShape(16.dp))
        ) {
            // Image content
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 100f
                        )
                    )
            )

            // Text overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Published: ${article.publishedAt}", // Format this as you need
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                )
            }

            // Loading placeholder for the image
            if (painter.state is AsyncImagePainter.State.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                        .placeholder(visible = true, color = Color.LightGray)
                )
            }
        }
    }
}


@Composable
fun NewsItem(article: Article, onClick: (article: Article) -> Unit) {
    val painter = rememberAsyncImagePainter(article.urlToImage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable { onClick(article) }
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .padding(5.dp)
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(25.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if (painter.state is AsyncImagePainter.State.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)
                            .placeholder(visible = true, color = Color.LightGray)
                    )
                }

                // Add subtle gradient for better text contrast
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Published: ${article.publishedAt}", // Assuming `publishedAt` is available
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Black,
                        fontSize = 8.sp
                    ), modifier = Modifier.background(color = Color(0xFF00695C).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)).padding(4.dp)
                )
            }
        }
    }
}


@Composable
fun CategoryItem(
    category: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF00695C) else Color(0xFFE0E0E0),
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = category.capitalize(Locale.ROOT),
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedBreakingNewsPager(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        articles.size
    }

    // Auto-scroll effect
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(4000) // Change slide every 4 seconds
            val nextPage = (pagerState.currentPage + 1) % articles.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    Column {


        Box(modifier = Modifier) {
            // Horizontal Pager with animations
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(end = 64.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Adjust pager height
                    .padding(vertical = 4.dp)
            ) { page ->
                // Enhanced breaking news item with animation
                BreakingNewsItem(
                    article = articles[page],
                    onClick = { onArticleClick(articles[page]) }
                )
            }
        }

        // Page Indicator Dots


    }
}


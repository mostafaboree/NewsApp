package com.example.newsapi.ui.Source

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.newsapi.R
import com.example.newsapi.data.model.Category
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SourceScreen(
    list:  List<Category>,
    navController: NavController,
    onItemClick: (String, String) -> Unit
) {
    val groupedItems = list.groupBy { it.category }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val categoryIndices = groupedItems.keys.toList()

    Column(modifier = Modifier.fillMaxSize()) {
        // Category selector at the top
        CategorySelector(categories = categoryIndices) { selectedCategory ->
            val index = categoryIndices.indexOf(selectedCategory)
            if (index != -1) {
                coroutineScope.launch {
                    listState.animateScrollToItem(index)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            state = listState
        ) {
            groupedItems.forEach { (category, list) ->
                stickyHeader {
                    CategoryHeader(title = category, category = category)
                }
                item {
                    ExpandableCategorySection(
                        title = category,
                        items = list,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableCategorySection(
    title: String,
    items: List<Category>,
    initiallyExpanded: Boolean = true,
    onItemClick: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null
            )
        }
        AnimatedVisibility(visible = expanded) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    NewsSourceItem(newsSource = item) {
                        onItemClick(item.url, item.name)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(title: String,category: String) {
    val gradientColors = when (category) {
        "technology" -> listOf(Color(0xFF1E88E5), Color(0xFF42A5F5)) // Blue tones
        "sports" -> listOf(Color(0xFF43A047), Color(0xFF66BB6A)) // Green tones
        "general" -> listOf(Color(0xFF6D4C41), Color(0xFF8D6E63)) // Brown tones
        "entertainment" -> listOf(Color(0xFF8E24AA), Color(0xFFAB47BC)) // Purple tones
        else -> listOf(Color(0xFFB00020), Color(0xFF6200EE)) // Default red-purple
    }

    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
        color = Color.White,
        modifier = Modifier
            .padding(8.dp)
            .background(
                brush = Brush.linearGradient(colors = gradientColors),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun NewsSourceItem(newsSource: Category, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp) // Fixed width to make it consistent in LazyRow
            .padding(8.dp)
            .clickable { onItemClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color(0xFFF9F9F9)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Load image using Coil or any other image loading library
            DynamicLogo(text = newsSource.name, category = newsSource.category)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = newsSource.name,
                style = MaterialTheme.typography.h6,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = newsSource.description,
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "Language: ${newsSource.language.uppercase()}",
                    style = MaterialTheme.typography.caption,
                    color = Color.DarkGray
                )
            }
        }
    }
}




@Composable
fun DynamicLogo(text: String, category: String) {
    val gradientColors = when (category) {
        "technology" -> listOf(Color(0xFF1E88E5), Color(0xFF42A5F5)) // Blue tones
        "sports" -> listOf(Color(0xFF43A047), Color(0xFF66BB6A)) // Green tones
        "general" -> listOf(Color(0xFF6D4C41), Color(0xFF8D6E63)) // Brown tones
        "entertainment" -> listOf(Color(0xFF8E24AA), Color(0xFFAB47BC)) // Purple tones
        else -> listOf(Color(0xFFB00020), Color(0xFF6200EE)) // Default red-purple
    }

    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.5f),
                offset = Offset(4f, 4f),
                blurRadius = 8f
            )
        ),
        modifier = Modifier
            .background(
                Brush.linearGradient(colors = gradientColors),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .fillMaxWidth()
    )
}
@Composable
fun CategorySelector(categories: List<String>, onCategorySelected: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf("") }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            Button(
                onClick = {
                    selectedCategory = category
                    onCategorySelected(category)
                },
                colors = androidx.compose.material.ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedCategory == category) Color.Gray else Color.LightGray
                ),
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = category.capitalize(), color = Color.Black)
            }
        }
    }
}

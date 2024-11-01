import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsapi.data.model.Article
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DetailsScreen(article: Article, navController: NavController,onClick: (String,String) -> Unit) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(bottom = 60.dp)) {
        // Main content area
        LazyColumn(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding =PaddingValues(bottom = 26.dp)) {
            item {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                Text(
                    text = "By ${article.author} | ${formatDate(article.publishedAt)}",
                    style = MaterialTheme.typography.subtitle2,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp).background(Color(0xFF00695C).copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)).padding(8.dp)
                )
            }
            item {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text(
                    text = if (isExpanded) article.description else article.description.take(100) + "...",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextButton(onClick = { isExpanded = !isExpanded }) {
                    Text(text = if (isExpanded) "See Less" else "See More")
                }
            }
            item {
                Text(
                    text = article.content,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                Text(
                    text = "Source: ${article.source.name}",
                    style = MaterialTheme.typography.caption,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp).background(Color(0xFF00695C).copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)).padding(8.dp)
                )
            }
        }

        // Floating Action Button Menu

            FloatingActionMenu(article, onClick = {onClick(article.title,article.url)})

    }
}



fun formatDate(dateString: String): String {
    val zonedDateTime = ZonedDateTime.parse(dateString)
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
    return zonedDateTime.format(formatter)
}
@Composable
fun FloatingActionMenu(article: Article,onClick: () -> Unit ) {
    val context = LocalContext.current
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            if (isMenuExpanded) {
                FloatingActionButton(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, article.url)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    },
                    backgroundColor = Color(0xFF00695C),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }

                // Button: Open
                FloatingActionButton(
                    onClick = onClick,
                    backgroundColor = Color(0xFF03DAC5),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = "Open in Browser",
                        tint = Color.White
                    )
                }

                // Button: Save
                FloatingActionButton(
                    onClick = {
                        // Implement save action
                        Log.d("FloatingActionMenu", "Save clicked")
                    },
                    backgroundColor = Color(0xFF4CAF50),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Save",
                        tint = Color.White
                    )
                }
            }

            // Main Floating Action Button
            FloatingActionButton(
                onClick = { isMenuExpanded = !isMenuExpanded },
                backgroundColor = if (isMenuExpanded) Color.Red else Color(0xFF00695C)
            ) {
                Icon(
                    imageVector = if (isMenuExpanded) Icons.Default.Close else Icons.Default.MoreVert,
                    contentDescription = "Expand Menu",
                    tint = Color.White
                )
            }
        }
    }
}


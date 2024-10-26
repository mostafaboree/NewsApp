package com.example.newsapi.ui.Composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Source
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun BottomNavBar(onItemClick: (BottomTab) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }


    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        val items = listOf("Home", "Source", "Bookmark")
        val icons = listOf(Icons.Filled.Home, Icons.Filled.Source, Icons.Filled.Bookmark)

        items.forEachIndexed { index, item ->
            val selected = selectedIndex == index
            val animatedColor = animateColorAsState(
                targetValue = if (selected) Color.Blue else Color.Gray
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item,
                        tint = animatedColor.value
                    )
                },
                label = { Text(item) },
                selected = selected,
                onClick = {
                    selectedIndex = index
                    onItemClick(if (index == 0) BottomTab.Home else if (index == 1) BottomTab.Source else BottomTab.Bookmark)
                },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

sealed class BottomTab {
    data object Home : BottomTab()
    data object Source : BottomTab()
    data object Bookmark : BottomTab()
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavBar() {
    BottomNavBar {
        when (it) {
            is BottomTab.Home -> {
                println("Home")
            }
            is BottomTab.Source -> {
                println("Source")
            }
            is BottomTab.Bookmark -> {
                println("Bookmark")
            }
        }


    }
}
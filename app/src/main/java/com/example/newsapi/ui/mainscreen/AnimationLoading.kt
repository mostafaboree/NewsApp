package com.example.booksshoe.presentation.ListScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun LoadingScreen() {

    LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp))
     {

        items(10){
    LoadingItem(modifier = Modifier)
        }}
}

@Composable
fun LoadingAppScreen() {
    LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)){
        item {
            LazyRow {
                items(5) {
                    CatogeryItem(modifier = Modifier)
                }
            }
        }

        items(5) {

            LoadingItem(modifier = Modifier)


        }

    }
}

@Composable
fun LoadingItem(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)

    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .ShimmerloadingAnimation(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .ShimmerloadingAnimation(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight(.6f)
                        .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .ShimmerloadingAnimation()
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.height(1.dp))
                Box(
                    modifier = modifier
                        .padding(10.dp)
                        .fillMaxWidth(1f)
                        .fillMaxHeight(0.3f)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .ShimmerloadingAnimation()
                )
                // Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = modifier
                        .padding(10.dp)
                        .fillMaxWidth(1f)
                        .fillMaxHeight(.5f)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .ShimmerloadingAnimation()
                )


            }
        }

    }
}
@Composable
fun CatogeryItem(modifier: Modifier) {
Box(
modifier = Modifier
    .width(100.dp)
    .height(50.dp)

) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .ShimmerloadingAnimation(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .ShimmerloadingAnimation(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .ShimmerloadingAnimation()
                    .clip(RoundedCornerShape(20.dp))
            )}}}}



fun Modifier.ShimmerloadingAnimation(
    animationDuration: Int = 1000,
    angleofAxisY: Float = 270f,
    widthOfBrush : Float = 300f
): Modifier {
    return composed{
        val shimmerColor = listOf(
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.0f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.3f)
        )

        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (animationDuration+widthOfBrush),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = animationDuration, easing = LinearEasing), repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        this.background(brush = Brush.linearGradient(colors = shimmerColor,
            start = Offset(x=translateAnimation.value - widthOfBrush,y=0f),
            end = Offset(translateAnimation.value,angleofAxisY)
        ))


    }}

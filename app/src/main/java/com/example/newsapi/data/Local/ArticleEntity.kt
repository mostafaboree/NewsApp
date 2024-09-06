package com.example.newsapi.data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import com.example.newsapi.data.model.Source

@Entity(tableName = "Article")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true) val key: Int,
    @Embedded val source: Source?,
    val url:String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)


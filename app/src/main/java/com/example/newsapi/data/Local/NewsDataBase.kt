package com.example.newsapi.data.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapi.data.model.Article
import com.example.newsapi.data.model.Category

@Database(entities = [ ArticleEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}

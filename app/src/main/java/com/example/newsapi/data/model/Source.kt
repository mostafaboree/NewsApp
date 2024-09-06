package com.example.newsapi.data.model

import androidx.room.PrimaryKey

data class Source(
    @PrimaryKey(autoGenerate = true) val id: String,
    val name: String
)
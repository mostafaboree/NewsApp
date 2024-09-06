package com.example.newsapi.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.newsapi.data.Local.NewsDao
import com.example.newsapi.data.Local.NewsDatabase
import com.example.newsapi.data.remote.NewsApiService
import com.example.newsapi.data.NewsRepositoryImpl
import com.example.newsapi.domin.NewsRepository
import com.example.newsapi.domin.use_case.GetArticlesUseCase
import com.example.newsapi.domin.use_case.GetArticlesUseCaseImpl
import com.example.newsapi.utls.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        return Retrofit.Builder()
            .baseUrl("https://saurav.tech/NewsAPI/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

   @Provides
    @Singleton
    fun provideNewsDatabase( context: Application): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "Article"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.newsDao()
    }
    @Provides
    @Singleton
   fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
   }

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: NewsApiService,newsDao: NewsDao,networkMonitor: NetworkMonitor): NewsRepository {
        return NewsRepositoryImpl(apiService,newsDao,networkMonitor)
    }



    @Provides
    @Singleton
    fun provideGetArticlesUseCase(repository: NewsRepository): GetArticlesUseCase {
        return GetArticlesUseCaseImpl(repository)
    }
}


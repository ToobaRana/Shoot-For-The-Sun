package com.example.sunandmoon.dependencyInjection

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.sunandmoon.data.localDatabase.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton // @Singleton makes sure all the view-models get the same instance of the database
    fun provideAppDatabase(app:Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java, "database-name"
        ).build()
    }
}

/*
@Module
class AppModule(private val context: Context) {
    @Provides
    fun provideAppDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }
}*/
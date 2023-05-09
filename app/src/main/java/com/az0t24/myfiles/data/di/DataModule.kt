package com.az0t24.myfiles.data.di

import android.content.Context
import androidx.room.Room
import com.az0t24.myfiles.data.FileHashDao
import com.az0t24.myfiles.data.FileHashRepository
import com.az0t24.myfiles.data.MyFilesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

private const val DB_NAME = "my_files_db"

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    @Named(value = DB_NAME)
    fun provideDatabaseName(): String {
        return DB_NAME
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @Named(value = DB_NAME) dbname: String,
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, MyFilesDatabase::class.java, dbname)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideFileHashDao(
        appDatabase: MyFilesDatabase
    ) = appDatabase.fileHashDao()


    @Singleton
    @Provides
    fun provideFileHashRepository(
        dao: FileHashDao,
    ) = FileHashRepository(dao)
}
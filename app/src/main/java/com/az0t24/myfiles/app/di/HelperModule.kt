package com.az0t24.myfiles.app.di

import android.content.Context
import com.az0t24.myfiles.app.FileHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HelperModule {
    @Provides
    @Singleton
    fun provideFileHelper(
        @ApplicationContext context: Context
    ) = FileHelper(context)
}

package com.rcdev.sqlitebrowser.di

import android.content.Context
import com.rcdev.sqlitebrowser.data.repository.SqliteRepository
import com.rcdev.sqlitebrowser.data.repository.SqliteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSqliteRepository(
        @ApplicationContext context: Context
    ): SqliteRepository {
        return SqliteRepositoryImpl(context)
    }
}

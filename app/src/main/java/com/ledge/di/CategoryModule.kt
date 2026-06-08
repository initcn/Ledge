package com.ledge.di

import com.ledge.data.provider.AssetCategoryProvider
import com.ledge.domain.CategoryProvider // <-- FIXED IMPORT
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryModule {

    @Binds
    @Singleton
    abstract fun bindCategoryProvider(
        provider: AssetCategoryProvider
    ): CategoryProvider
}
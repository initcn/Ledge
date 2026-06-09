package app.initcn.ledge.di

import app.initcn.ledge.data.provider.AssetCategoryProvider
import app.initcn.ledge.domain.CategoryProvider
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
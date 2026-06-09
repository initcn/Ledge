package app.initcn.ledge.data.provider

import android.content.Context
import app.initcn.ledge.data.entity.CategoriesModel
import app.initcn.ledge.domain.CategoryProvider
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetCategoryProvider @Inject constructor(

    @param:ApplicationContext
    private val context: Context

) : CategoryProvider {

    private val cachedCategories by lazy {

        loadCategories()
    }

    override fun getCategories():
            CategoriesModel {

        return cachedCategories
    }

    private fun loadCategories():
            CategoriesModel {

        val json = context.assets

            .open("categories.json")

            .bufferedReader()

            .use {
                it.readText()
            }

        return Gson().fromJson(

            json,

            CategoriesModel::class.java

        ) ?: CategoriesModel()
    }
}
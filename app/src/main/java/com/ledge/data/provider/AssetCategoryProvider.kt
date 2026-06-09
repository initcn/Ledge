package com.ledge.data.provider

import android.content.Context
import com.google.gson.Gson
import com.ledge.data.entity.CategoriesModel
import com.ledge.domain.CategoryProvider
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
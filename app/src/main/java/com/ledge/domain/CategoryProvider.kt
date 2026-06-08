package com.ledge.domain

import com.ledge.data.entity.CategoriesModel

interface CategoryProvider {
    fun getCategories(): CategoriesModel
}
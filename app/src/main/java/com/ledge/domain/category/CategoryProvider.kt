package com.ledge.domain.category

import com.ledge.data.entity.CategoriesModel

interface CategoryProvider {

    fun getCategories():
            CategoriesModel
}
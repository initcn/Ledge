package app.initcn.ledge.domain

import app.initcn.ledge.data.entity.CategoriesModel

interface CategoryProvider {
    fun getCategories(): CategoriesModel
}
package app.initcn.ledge.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(

    tableName = "budgets",

    indices = [

        Index(

            value = [

                "category",
                "month",
                "year"
            ],

            unique = true
        )
    ]
)
data class BudgetEntity(

    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int = 0,

    val category: String,

    val limitAmount: Long,

    val month: Int,

    val year: Int
)
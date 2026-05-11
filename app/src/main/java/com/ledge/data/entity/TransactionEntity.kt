package com.ledge.data.entity


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import com.ledge.core.model.TransactionType

@Entity(

    tableName = "transactions",

    indices = [

        Index(
            value = ["createdAt"]
        ),

        Index(
            value = ["type"]
        ),

        Index(
            value = ["category"]
        ),

        Index(
            value = ["mode"]
        )
    ]
)
data class TransactionEntity(

    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int = 0,

    val type: TransactionType,

    val amount: Long,

    val category: String,

    val mode: String,

    val note: String,

    val createdAt: Long =
        System.currentTimeMillis()
)
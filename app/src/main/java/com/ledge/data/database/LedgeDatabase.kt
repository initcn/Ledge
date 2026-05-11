package com.ledge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.ledge.data.dao.BudgetDao
import com.ledge.data.dao.TransactionDao

import com.ledge.data.entity.BudgetEntity
import com.ledge.data.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 5,
    exportSchema = false
)

@TypeConverters(Converters::class)

abstract class LedgeDatabase :
    RoomDatabase() {

    abstract fun transactionDao():
            TransactionDao

    abstract fun budgetDao():
            BudgetDao
}
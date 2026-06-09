package app.initcn.ledge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.initcn.ledge.data.dao.BudgetDao
import app.initcn.ledge.data.dao.TransactionDao
import app.initcn.ledge.data.entity.BudgetEntity
import app.initcn.ledge.data.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LedgeDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun budgetDao(): BudgetDao
}
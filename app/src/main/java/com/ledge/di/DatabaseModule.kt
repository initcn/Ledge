package com.ledge.di

import android.content.Context
import androidx.room.Room
import com.ledge.data.backup.BackupManager
import com.ledge.data.dao.BudgetDao
import com.ledge.data.dao.TransactionDao
import com.ledge.data.database.LedgeDatabase
import com.ledge.data.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(

        @ApplicationContext
        context: Context

    ): LedgeDatabase {

        return Room.databaseBuilder(

            context,

            LedgeDatabase::class.java,

            "ledge_database"

        )

            .fallbackToDestructiveMigration(
                false
            )

            .build()
    }

    @Provides
    fun provideTransactionDao(

        database: LedgeDatabase

    ): TransactionDao {

        return database
            .transactionDao()
    }

    @Provides
    fun provideBudgetDao(

        database: LedgeDatabase

    ): BudgetDao {

        return database
            .budgetDao()
    }

    @Provides
    @Singleton
    fun provideBackupManager(

        repository:
        TransactionRepository,

        budgetDao:
        BudgetDao

    ): BackupManager {

        return BackupManager(

            repository = repository,

            budgetDao = budgetDao
        )
    }
}
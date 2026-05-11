package com.ledge.data.backup

import android.content.Context
import com.google.gson.Gson
import com.ledge.data.dao.BudgetDao
import com.ledge.data.repository.TransactionRepository
import java.io.File

class BackupManager(

    private val repository:
    TransactionRepository,

    private val budgetDao:
    BudgetDao
) {

    suspend fun exportBackup(

        context: Context

    ): File {

        val backupData =

            BackupData(

                transactions =

                    repository
                        .getAllTransactionsList(),

                budgets =

                    budgetDao
                        .getAllBudgets()
            )

        val json =

            Gson()
                .toJson(backupData)

        val file =

            File(

                context.cacheDir,

                "ledge_backup.json"
            )

        file.writeText(json)

        return file
    }

    suspend fun restoreBackup(

        json: String
    ) {

        val backupData =

            Gson().fromJson(

                json,

                BackupData::class.java
            )

        repository
            .clearTransactions()

        budgetDao
            .clearBudgets()

        repository
            .insertTransactions(

                backupData.transactions
            )

        budgetDao
            .insertBudgets(

                backupData.budgets
            )
    }
}
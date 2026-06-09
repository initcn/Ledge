package app.initcn.ledge.data.backup

import android.content.Context
import app.initcn.ledge.data.dao.BudgetDao
import app.initcn.ledge.data.repository.TransactionRepository
import com.google.gson.Gson
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
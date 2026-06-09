package app.initcn.ledge.data.backup

import app.initcn.ledge.data.entity.BudgetEntity
import app.initcn.ledge.data.entity.TransactionEntity

data class BackupData(

    val transactions:
    List<TransactionEntity>,

    val budgets:
    List<BudgetEntity>
)
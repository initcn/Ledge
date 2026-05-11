package com.ledge.data.backup

import com.ledge.data.entity.BudgetEntity
import com.ledge.data.entity.TransactionEntity

data class BackupData(

    val transactions:
    List<TransactionEntity>,

    val budgets:
    List<BudgetEntity>
)
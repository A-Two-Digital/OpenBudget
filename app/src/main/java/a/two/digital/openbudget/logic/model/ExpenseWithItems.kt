package a.two.digital.openbudget.logic.model

import a.two.digital.openbudget.data.entity.Expense
import a.two.digital.openbudget.data.entity.ExpenseItem
import androidx.room.Embedded
import androidx.room.Relation

data class ExpenseWithItems(
    @Embedded val expense: Expense,

    @Relation(
        parentColumn = "id",
        entityColumn = "expenseId"
    )
    val items: MutableList<ExpenseItem>
)

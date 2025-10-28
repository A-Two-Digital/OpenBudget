package a.two.digital.openbudget.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenseItems",
    foreignKeys = [ForeignKey(
        entity = ExpenseType::class,
        parentColumns = ["id"],
        childColumns = ["expenseTypeId"],
        onDelete = ForeignKey.RESTRICT
    ), ForeignKey(
        entity = Expense::class,
        parentColumns = ["id"],
        childColumns = ["expenseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val price: Double,
    val expenseTypeId: Int,
    val expenseId: Int
)

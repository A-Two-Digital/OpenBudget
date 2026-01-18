package a.two.digital.openbudget.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenseItems",
    foreignKeys = [ForeignKey(
        entity = Expense::class,
        parentColumns = ["id"],
        childColumns = ["expenseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String,
    val price: Double,
    val expenseId: Long
)

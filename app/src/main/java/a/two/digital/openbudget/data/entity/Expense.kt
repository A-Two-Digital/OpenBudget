package a.two.digital.openbudget.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = ExpenseType::class,
        parentColumns = ["id"],
        childColumns = ["expenseTypeId"],
        onDelete = ForeignKey.RESTRICT
    )]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val date: Long,
    val expenseTypeId: Int,
    val isIncoming: Boolean,
    val isRecurring: Boolean
)

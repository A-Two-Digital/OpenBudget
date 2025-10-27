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
    val id: Int = 0,
    val expenseTypeId: Int,
    val date: Long,
    val description: String,
    val price: Float
)

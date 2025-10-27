package a.two.digital.openbudget.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenseTypes")
class ExpenseType(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val color: String
)
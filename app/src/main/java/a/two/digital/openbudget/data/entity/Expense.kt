package a.two.digital.openbudget.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val date: Long,
    val isIncoming: Boolean,
    val isRecurring: Boolean
)

package a.two.digital.openbudget

import a.two.digital.openbudget.data.AppDatabase
import a.two.digital.openbudget.ui.theme.OpenBudgetTheme
import a.two.digital.openbudget.ui.theme.Purple40
import a.two.digital.openbudget.ui.theme.Purple80
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    private val database: AppDatabase by lazy {
        (application as OpenBudgetApplication).database
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenBudgetTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = { ExpenseFAB() },
                ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ExpenseFAB() {
    FloatingActionButton(
        onClick = { /* TODO */ },
        containerColor = Purple40,
        contentColor = Purple80
    ) {
        Icon(Icons.Filled.Add, "Add an expense")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenBudgetTheme {
        Greeting("Android")
    }
}
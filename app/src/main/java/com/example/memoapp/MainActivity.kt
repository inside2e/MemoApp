package com.example.memoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.memoapp.ui.theme.MemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoAppTheme {
                MyApplication()
            }
        }
    }
}

@Composable
fun MyApplication() {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavigationGraph(navController)
    }
}

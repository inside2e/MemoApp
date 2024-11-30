package com.example.memoapp.function

import androidx.compose.ui.graphics.Color


fun importanceToColor(importance:Int): Color {
    return when (importance) {
        1 -> Color.Red
        2 -> Color.Yellow
        3 -> Color.Green
        else -> Color.Gray
    }
}
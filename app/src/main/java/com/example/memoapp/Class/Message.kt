package com.example.memoapp.Class

import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class Message(
    var title:String="",
    var time: String = getCurrentTime(),
    var importance:Int=4,
    var mes:String=""
    ) {
    companion object {
        fun getCurrentTime(): String {
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        }
    }
}


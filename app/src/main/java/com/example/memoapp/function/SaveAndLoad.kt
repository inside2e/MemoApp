package com.example.memoapp.function

import android.content.Context
import android.util.Log
import com.example.memoapp.Class.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStreamWriter

fun saveMemos(context: Context, fileName: String, memos: List<Message>) {
    try{
        val gson = Gson()
        val json = gson.toJson(memos)
        val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        BufferedWriter(OutputStreamWriter(fileOutputStream)).use { writer ->
            writer.write(json)
        }
    }catch(e:Exception){
        Log.w("1","error")
    }

}

fun readMemos(context: Context, fileName: String): List<Message> {
    val fileInputStream = context.openFileInput(fileName)
    val json = fileInputStream.bufferedReader().use { it.readText() } // 从文件读取 JSON 字符串
    val type = object : TypeToken<List<Message>>() {}.type
    return Gson().fromJson(json, type)
}

fun SetEmptyFile(context: Context, filename: String) {
    try{
    val fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
    BufferedWriter(OutputStreamWriter(fileOutputStream)).use { writer ->
        writer.write("")
    }
    }catch(e:Exception){
        Log.w("1","error")
    }
}
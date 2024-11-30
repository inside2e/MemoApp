package com.example.memoapp.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.memoapp.Class.Message
import com.example.memoapp.SystemConstants
import com.example.memoapp.function.readMemos
import com.example.memoapp.function.saveMemos
import java.util.Calendar

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewMessageView(navHostController: NavHostController) {
    var titleValue by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("无") }
    var contentValue by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var context = LocalContext.current
    val filename = SystemConstants.FILENAME
    var memos by remember { mutableStateOf(listOf<Message>()) }
    LaunchedEffect(Unit) {
        try{
            memos = readMemos(context, filename)
        }catch (e:Exception){
            Log.w("1","该文件不存在")
        }
    }
    Column() {
        TopAppBar(
            windowInsets = TopAppBarDefaults.windowInsets,
            title = @Composable { Text("添加备忘录") },
            navigationIcon = {
                IconButton(onClick = { navHostController.navigate("main") }) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                }
            },
            colors = TopAppBarColors(
                Color.Blue,
                Color.Blue,
                Color.White,
                Color.White,
                Color.White
            ),
            actions = {
                IconButton(onClick = {
                    val mes = Message(
                        title = titleValue,
                        mes = contentValue,
                        importance = SelectedToImportance(selectedOption),
                        time = selectedDate
                    )
                    memos = memos + mes
                    val filename = SystemConstants.FILENAME
                    saveMemos(context, filename, memos)
                    navHostController.navigate("main")
                }) {
                    Icon(Icons.Filled.Check, contentDescription = null)
                }
            }
        )
        Column() {
            TextField(
                value = titleValue,
                onValueChange = { newValue -> titleValue = newValue },
                modifier = Modifier.fillMaxWidth()
                    .padding(6.dp),
                label = { Text("请输入标题") }
            )
            Card(
                modifier = Modifier.padding(3.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "重要性",
                    fontSize = 21.sp,
                    modifier = Modifier.padding(all = 7.dp),
                    fontWeight = FontWeight.Bold
                )
                Row() {
                    RadioButtonOption(
                        "非常重要",
                        selectedOption,
                        onSelect = { selectedOption = "非常重要" })
                    RadioButtonOption(
                        "重要",
                        selectedOption,
                        onSelect = { selectedOption = "重要" })
                    RadioButtonOption(
                        "一般",
                        selectedOption,
                        onSelect = { selectedOption = "一般" })
                    RadioButtonOption(
                        "无",
                        selectedOption,
                        onSelect = { selectedOption = "无" })
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            TextField(
                value = selectedDate,
                onValueChange = {},
                label = { Text("选择的日期") },
                readOnly = true
            )
            Button(onClick = { showDatePickerDialog = true }) {
                Text("选择日期", fontSize = 12.sp)
            }
            if (showDatePickerDialog) {
                val currentDate = Calendar.getInstance()
                val year = currentDate.get(Calendar.YEAR)
                val month = currentDate.get(Calendar.MONTH)
                val day = currentDate.get(Calendar.DAY_OF_MONTH)
                showDatePickerDialog = false      // 隐藏对话框
                DatePickerDialog(
                    LocalContext.current,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        // 格式化所选择的日期
                        selectedDate = String.format(
                            "%04d-%02d-%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay
                        )

                    },
                    year,
                    month,
                    day,
                ).apply {
                    setCancelable(true)
                    setCanceledOnTouchOutside(true)
                }.show()
            }
        }
        TextField(
            value = contentValue,
            onValueChange = { newValue -> contentValue = newValue },
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(),
            label = { Text("请输入内容") }
        )
    }
}

@Composable
fun RadioButtonOption(option: String, selectedOption: String, onSelect: () -> Unit) {
    Row(
        modifier = Modifier.selectable(
            selected = (option == selectedOption),
            onClick = onSelect
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (option == selectedOption),
            onClick = onSelect
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = option)
    }
}

fun SelectedToImportance(selectedOption: String):Int{
    return when(selectedOption){
        "非常重要" -> 1
        "重要" -> 2
        "一般" -> 3
        else -> 4
    }
}

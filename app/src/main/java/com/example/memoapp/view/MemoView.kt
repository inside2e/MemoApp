package com.example.memoapp.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.memoapp.Class.Message
import com.example.memoapp.SystemConstants
import com.example.memoapp.function.SetEmptyFile
import com.example.memoapp.function.readMemos
import com.example.memoapp.function.saveMemos

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MemoView(navController: NavHostController, index:Int) {
    // 左上角返回，右上角删除，标题，正文，可滑动，时间显示在最上方
    val context = LocalContext.current
    val filename = SystemConstants.FILENAME
    var memos by remember { mutableStateOf(listOf<Message>()) }
    LaunchedEffect(Unit) {
        try{
            memos = readMemos(context, filename)
        }catch (e:Exception){
            Log.w("妖","该文件不存在")
        }
    }
    var mes = memos.getOrNull(index)
    if(mes == null){
        mes = Message()
    }
    var showdeleteDialog by remember { mutableStateOf(false) }
    Column() {
        TopAppBar(
            windowInsets = TopAppBarDefaults.windowInsets,
            title = @Composable { Text(mes!!.time) },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("main") }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
            colors = TopAppBarColors(Color.Blue, Color.Blue, Color.White, Color.White, Color.White),
            actions = {
                IconButton(onClick = {
                    showdeleteDialog = true }) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = mes!!.title,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )  //标题
            Text(
                text = mes!!.mes,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )//正文
        }

    }
    if (showdeleteDialog) {
        AlertDialog(
            onDismissRequest = { false.also { showdeleteDialog = it } }, // 点击对话框外侧或取消时关闭对话框
            title = { Text("确认删除") },
            text = { Text("您确定要删除这个备忘录吗？") },
            confirmButton = {
                Button(onClick = {
                    // 进行删除操作
                    memos = memos.filterIndexed { i, _ -> i != index }
                    if(!memos.isEmpty()){
                        saveMemos(context, filename, memos) // 更新文件
                    }else{
                        SetEmptyFile(context,filename)
                        Log.e("更新","更新为空列表")
                    }
                    navController.navigate("main")
                }) {
                    Text("是")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showdeleteDialog = false // 关闭对话框
                }) {
                    Text("否")
                }
            }
        )
    }
}
package com.example.memoapp.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.memoapp.Class.Message
import com.example.memoapp.SystemConstants
import com.example.memoapp.function.importanceToColor
import com.example.memoapp.function.readMemos

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainView(navController: NavHostController, modifier: Modifier = Modifier) {
    //读取文件信息，保存入变量，生成内容
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
    Column() {
        TopAppBar(
            windowInsets = TopAppBarDefaults.windowInsets,
            title = @Composable { Text("备忘录") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("add") }) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            },
            colors = TopAppBarColors(
                Color.Blue,
                Color.Blue,
                Color.White,
                Color.White,
                Color.White
            )
        )
        FlowRow(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            maxItemsInEachRow = 2,
        ) {
            if(memos.size!=0){
                for (i in 0..memos.size - 1) {
                    MessageCard(memos[i], navController, i)
                }
            }
        }
    }

}


@Composable
fun MessageCard(message: Message,navController:NavHostController,index:Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .requiredHeight(250.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = {navController.navigate("memo?index=${index}")}
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = message.title,
                    fontSize = 25.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )  //标题
                Text(
                    text = message.mes,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )//正文
                Text(
                    text = message.time,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )//时间

            }
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color = importanceToColor(message.importance), shape = CircleShape)
                    .align(Alignment.BottomEnd)
            )//重要性
        }
    }
}
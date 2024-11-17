package com.example.marvalapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionDetails(sectionName:String,detail:String){
    Column {
        Text(
            text = sectionName,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = detail,
            style = TextStyle(fontSize = 16.sp),
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
package com.example.marvalapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.entity.character.CharacterDetail
import com.example.domain.entity.character.Item
import com.example.marvalapp.R


@Composable
fun SectionCharactersImages(modifier: Modifier=Modifier, sectionTitle:String, characterDetail: CharacterDetail,onClickItem:(List<Item>,Int)->Unit) {
    Column (modifier = modifier){
        Text(
            text = sectionTitle,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(characterDetail.items) { item ->
                Column(modifier = Modifier.width(100.dp).clickable { onClickItem(characterDetail.items,0)}) {
                    AsyncImage(
                        model = "${item.resourceURI}/standard_medium.jpg",
                        contentDescription = "",
                        Modifier
                            .fillMaxWidth().height(100.dp)
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        error =painterResource(R.drawable.default_image)
                    )

                    Text(item.name, style = TextStyle(fontSize = 12.sp), color = Color.White)
                }
            }
        }
    }

}
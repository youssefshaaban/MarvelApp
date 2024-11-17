package com.example.marvalapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.domain.entity.character.Item
import com.example.marvalapp.R
import com.example.marvalapp.utils.fromHex


@Composable
fun FullScreenImagePopup(
    imageRes: List<Item>,           // Resource ID of the image
    isVisible: Boolean,      // Visibility state of the popup
    onDismiss: () -> Unit    // Callback to dismiss the popup
) {
    if (isVisible) {
        Dialog(properties =DialogProperties( usePlatformDefaultWidth = false ),onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.fromHex("292C30"))
            ) {
                // Full-Screen Image
                // Close Icon
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Popup",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End)
                        .clickable { onDismiss() } // Dismiss on clicking the close icon
                )
                LazyRow {
                    items(imageRes, key = {it.resourceURI}){item: Item ->
                        Column(verticalArrangement = Arrangement.Center) {
                            Box ( modifier = Modifier
                                .weight(1f)){
                                AsyncImage(
                                    model = "${item.resourceURI}/standard_medium.jpg",
                                    contentDescription = "",
                                    Modifier
                                        .aspectRatio(1f/2f)
                                        .padding(top = 20.dp,end = 8.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop,
                                    error = painterResource(R.drawable.default_image)
                                )
                            }

                            Text(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp).align(Alignment.CenterHorizontally), text = item.name, style = TextStyle(fontSize = 16.sp), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

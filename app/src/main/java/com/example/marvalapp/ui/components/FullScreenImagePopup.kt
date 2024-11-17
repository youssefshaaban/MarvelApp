package com.example.marvalapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.domain.entity.character.Item
import com.example.marvalapp.R


@Composable
fun FullScreenImagePopup(
    imageRes: List<Item>,           // Resource ID of the image
    isVisible: Boolean,      // Visibility state of the popup
    onDismiss: () -> Unit    // Callback to dismiss the popup
) {
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Full-Screen Image
                LazyRow {
                    items(imageRes, key = {it.resourceURI}){item: Item ->
                        AsyncImage(
                            model = "${item.resourceURI}/standard_medium.jpg",
                            contentDescription = "",
                            Modifier
                                .fillMaxWidth().height(100.dp)
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.default_image)
                        )
                    }
                }
                // Close Icon
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Popup",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                        .clickable { onDismiss() } // Dismiss on clicking the close icon
                )
            }
        }
    }
}

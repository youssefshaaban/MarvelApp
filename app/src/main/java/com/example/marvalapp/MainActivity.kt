package com.example.marvalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Animatable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.marvalapp.ui.navigation.NavGraph
import com.example.marvalapp.ui.theme.MarvalAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
private fun MainScreen() {
    MarvalAppTheme {
        val navController = rememberNavController()
        Scaffold {paddingValues ->  NavGraph(modifier = Modifier.padding(paddingValues),navController) }

    }
}





@Composable
fun RecompositionTrackScreen(modifier: Modifier=Modifier) {
    val input = remember { mutableStateOf("") }
    val items = remember { mutableStateListOf<String>() }

    Column(modifier=modifier) {
        OutlinedTextField(
            value = input.value,
            onValueChange = {input.value=it},
            label = { Text(text = "enter Items") },
            modifier = Modifier
                .fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier.trackRecompositions(), onClick = {
            if (input.value.isNotBlank()) {
                items.add(input.value)
                input.value=""
            }
        }) {
            Text("Add item")
        }

        Spacer(modifier =Modifier.height(10.dp))

        LazyColumn {
            items(items){ item->
                Text(item)
            }
        }
    }
}





@Composable // Custom modifier to track recompositions correctly

// Custom modifier to track recompositions correctly
fun Modifier.trackRecompositions(): Modifier {
    // Store recomposition count
    val recompositionCount = remember { mutableStateOf(0) }
    val flashColor = remember { Animatable(Color.Red) }

    // Use SideEffect to track recompositions (increments only once per recomposition)
    SideEffect {
        recompositionCount.value += 1
    }


    // Draw content with a red border and recomposition count
    return this
        .then(
            Modifier.drawWithContent {
                drawContent() // Draw the original content
                val text = "Recompositions: ${recompositionCount.value}"
                drawIntoCanvas { canvas ->
                    val paint = android.graphics
                        .Paint()
                        .apply {
                            textSize = 40f
                            color = android.graphics.Color.RED
                            isAntiAlias = true
                        }
                    // Draw the recomposition count text below the content
                    canvas.nativeCanvas.drawText(
                        text,
                        10f,
                        size.height + 40f,
                        paint
                    )
                }
            }
        )
        .border(2.dp, flashColor.value) // Add red border around the component
        .padding(16.dp) // Add padding for space
}
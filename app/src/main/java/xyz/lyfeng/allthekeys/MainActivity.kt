package xyz.lyfeng.allthekeys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import xyz.lyfeng.allthekeys.ui.theme.AllTheKeysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AllTheKeysTheme {
                Playground()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Playground() {
    var text by remember { mutableStateOf("") }
    val events = remember { mutableStateListOf<KeyEvent>() }

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            arrayOf(

                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Editor") },
                    singleLine = false,
                    modifier = Modifier
                        .onKeyEvent {
                            events.add(it)
                        }
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                ),

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                ) {
                    items(events) {
                        KeyEventRow(it.nativeKeyEvent)
                    }
                }

            )
        }
    }
}

@Composable
fun KeyEventRow(event: NativeKeyEvent) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        arrayOf(
            // action
            when (event.action) {
                NativeKeyEvent.ACTION_DOWN -> "ACTION_DOWN: "
                NativeKeyEvent.ACTION_UP -> "ACTION_UP:   "
                NativeKeyEvent.ACTION_MULTIPLE -> "ACTION_MULT: "
                else -> "ACTION? (${event.action}): "
            },
            // code
            NativeKeyEvent.keyCodeToString(event.keyCode),
            // unicode
            "u:x" + event.unicodeChar.toString(16),
            // scan code
            "s:x" + event.scanCode.toString(16),
            // meta state
            "m:b" + event.metaState.toString(2),
            // repeat
            "r:d" + event.repeatCount.toString(),
        ).forEach { text -> Text(text, fontFamily = FontFamily.Monospace) }
    }
}

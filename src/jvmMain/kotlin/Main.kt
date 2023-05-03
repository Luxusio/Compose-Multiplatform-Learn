import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

@Composable
@Preview
fun App() {

    var count by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("Hello, World!") }
    var text2 by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Column {

            @OptIn(ExperimentalFoundationApi::class)
            Box(
                modifier = Modifier
                    .background(Color.Magenta)
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.2f)
                    .combinedClickable(
                        onClick = {
                            text2 = "Click ${count++}"
                        },
                        onDoubleClick = {
                            text2 = "Double Click ${count++}"
                        },
                        onLongClick = {
                            text2 = "Long Click ${count++}"
                        }
                    )
            )

            Text(text = text2, fontSize = 40.sp)

            AsyncImage(
                load = { loadImageBitmap("https://www.jetbrains.com/company/brand/img/jetbrains_logo.png") },
                paintFor = { remember { BitmapPainter(it) } },
                contentDescription = "JetBrains Logo",
                modifier = Modifier.width(200.dp)
            )

            Button(onClick = {
                count++
                text = "Hello, ${count}!"
            }) {
                Text(text)
            }
        }
    }
}

@Preview
@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    paintFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        Image(
            painter = paintFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

fun loadImageBitmap(url: String): ImageBitmap =
    URL(url).openStream().buffered().use(::loadImageBitmap)

fun main() = application {
    val icon = painterResource("images/sample.png")

    Tray(
        icon = icon,
        menu = {
            Item("Quit App", onClick = ::exitApplication)
        }
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp),
        icon = icon
    ) {
        App()
    }
}

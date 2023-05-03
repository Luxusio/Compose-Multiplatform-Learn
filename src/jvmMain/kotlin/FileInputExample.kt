import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.singleWindowApplication
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import java.awt.FileDialog


@Preview
fun main() = singleWindowApplication {
    var showFilePicker by remember { mutableStateOf(false) }

    FilePicker(showFilePicker) { path ->
        showFilePicker = false
        // do something with path
    }

    Column {

        Button(onClick = {
//        java.awt.FileDialog(window).isVisible = true
            showFilePicker = true
        }) {
            Text("Open File")
        }

        Button(onClick = {
            FileDialog(window, "Save", FileDialog.SAVE).apply {
                isVisible = true
                val file = this.file
                if (file != null) {
                    // do something with file
                    println(file)
                }
            }
        }) {
            Text("Save File")
        }
    }
}
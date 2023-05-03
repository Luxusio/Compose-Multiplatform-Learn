package state

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

/*

1. remember {} 함수의 작동 원리는 무엇인가?
 ->

2. by 키워드는 무슨 원리인가?




 */

@Composable
fun WellnessTaskItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = taskName
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}

@Composable
fun WellnessTaskItem(taskName: String, onClose: () -> Unit, modifier: Modifier = Modifier) {
    var checkedState by rememberSaveable { mutableStateOf(false) }

    WellnessTaskItem(
        taskName = taskName,
        checked = checkedState,
        onCheckedChange = { newValue -> checkedState = newValue },
        onClose = onClose, // we will implement this later!
        modifier = modifier,
    )
}

data class WellnessTask(val id: Int, val label: String)

fun getWellnessTasks(): List<WellnessTask> = List(30) { i ->
    WellnessTask(i, "Task #$i")
}


// viewmodel is not supported in kotiln compose multiplatform

//class WellnessViewModel : ViewModel() {
//    private val _tasks = getWellnessTasks().toMutableStateList()
//    val tasks: List<WellnessTask>
//        get() = _tasks
//
//    fun remove(item: WellnessTask) {
//        _tasks.remove(item)
//    }
//}

//private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }

@Composable
fun WellnessTasksList(
    list: List<WellnessTask>,
    onCloseTask: (WellnessTask) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = list, key = { task -> task.id }) { task ->
            WellnessTaskItem(taskName = task.label, onClose = { onCloseTask(task) })
        }
    }
}


@Preview
@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableStateOf(0) }
    var juiceCount by rememberSaveable { mutableStateOf(0) }

    // ---------
    Row {
        StatelessCounter(
            count = count,
            onIncrement = { count++ },
            modifier = modifier
        )
        StatelessCounter(
            count = juiceCount,
            onIncrement = { juiceCount++ },
            modifier = modifier
        )
    }
}

@Composable
private fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        if (count > 0) {
            Text(text = "You've had $count glasses.")
        }

        Button(
            onClick = onIncrement,
            enabled = count < 10,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Add one")
        }
    }
}

@Composable
fun WellnessScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        WaterCounter(modifier = modifier)

        val list = remember { getWellnessTasks().toMutableStateList() }
//        val list = remember {
//            mutableStateListOf<WellnessTask>().apply {
//                addAll(getWellnessTasks())
//            }
//        }
        WellnessTasksList(list = list, onCloseTask = { task -> list.remove(task) }, modifier = modifier)
    }
}

fun main() = singleWindowApplication(title = "할카") {
    WellnessScreen()
}
package navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.parcelable.Parcelable


data class Item(
    val id: Long,
    val text: String
)

interface Database {
    fun getAll(): List<Item>
    fun getById(id: Long): Item
}

@Composable
fun ItemListScreen(items: List<Item>, onItemClick: (id: Long) -> Unit) {
    LazyColumn {
        items(items = items) { item ->
            Text(
                text = item.text,
                modifier = Modifier.clickable { onItemClick(item.id) }
            )
        }
    }
}

@Composable
fun ItemDetailsScreen(item: Item, onBackClick: () -> Unit) {
    Column {
        TopAppBar(
            title = { Text("Item details") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )

        Text(text = item.text)
    }
}

sealed class Configuration : Parcelable {
    object List : Configuration()
    data class Details(val itemId: Long) : Configuration()
}

class ItemList(
    database: Database,
    val onItemSelected: (ItemId: Long) -> Unit
) {
    private val _state = mutableStateOf(database.getAll())
    val state: State<List<Item>> = _state
}

@Composable
fun ItemListUi(list: ItemList) {
    ItemListScreen(
        items = list.state.value,
        onItemClick = list.onItemSelected
    )
}

class ItemDetails(
    itemId: Long, // An item id to be loaded and displayed
    database: Database, // Accept the Database as dependency
    val onFinished: () -> Unit // Called on TopAppBar back button click
) {
    // No concurrency involved just for simplicity. The state can be updated if needed.
    private val _state = mutableStateOf(database.getById(id = itemId))
    val state: State<Item> = _state
}

@Composable
fun ItemDetailsUi(details: ItemDetails) {
    ItemDetailsScreen(
        item = details.state.value,
        onBackClick = details.onFinished
    )
}
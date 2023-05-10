package navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

typealias Content = @Composable () -> Unit

fun <T : Any> T.asContent(content: @Composable (T) -> Unit): Content = { content(this) }

class Root(
    componentContext: ComponentContext, // In Decompose each component has its own ComponentContext
    private val database: Database // Accept the Database as dependency
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    private val stack =
        childStack(
            source = navigation,
            initialConfiguration = Configuration.List, // Starting with List
            childFactory = ::createChild // The Router calls this function, providing the child Configuration and ComponentContext
        )

    val childStack: Value<ChildStack<Configuration, Content>> = stack

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when (configuration) {
            is Configuration.List -> list()
            is Configuration.Details -> details(configuration)
        } // Configurations are handled exhaustively

    private fun list(): Content =
        ItemList(
            database = database, // Supply dependencies
            onItemSelected = { navigation.push(Configuration.Details(itemId = it)) } // Push Details on item click
        ).asContent { ItemListUi(it) }

    private fun details(configuration: Configuration.Details): Content =
        ItemDetails(
            itemId = configuration.itemId, // Safely pass arguments
            database = database, // Supply dependencies
            onFinished = navigation::pop // Go back to List
        ).asContent { ItemDetailsUi(it) }
}

@Composable
fun RootUi(root: Root) {
    val stack by root.childStack.subscribeAsState()
    Children(stack = stack) { child ->
        child.instance()
    }
}

fun main() {
//    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    val lifecycle = LifecycleRegistry()
    val root = root(DefaultComponentContext(lifecycle = lifecycle))


    application {
        singleWindowApplication(
            title = "Navigation tutorial"
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                MaterialTheme {
                    RootUi(root) // Render the Root and its children
                }
            }
        }
    }
}

private fun root(componentContext: ComponentContext): Root =
    // The rememberRootComponent function provides the root ComponentContext and remembers the instance or Root
    Root(
        componentContext = componentContext,
        database = DatabaseImpl() // Supply dependencies
    )

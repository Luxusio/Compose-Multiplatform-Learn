package sqldelight

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

/**
 * Created by kjkim on 2023/05/10.
 *
 * @author kjkim
 * @version Compose-Multiplatform-Learn
 * @since Compose-Multiplatform-Learn
 */
class DatabaseDriverFactory {
    fun create() = JdbcSqliteDriver("jdbc:sqlite:database.db")

    fun a() {
        val foo = create()

        AppDatabase
        // io.luxus.sqldelight.AppDatabase.Schema.create(foo)
    }
}

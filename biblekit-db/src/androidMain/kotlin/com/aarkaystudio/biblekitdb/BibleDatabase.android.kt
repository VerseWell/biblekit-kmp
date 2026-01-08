package com.aarkaystudio.biblekitdb

import android.content.Context
import android.content.ContextWrapper
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.osmerion.android.database.sqlite.OsmerionSQLiteOpenHelperFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

public actual class DriverFactory(
    private val context: Context,
    private val logBlock: ((String) -> Unit)? = null,
) {
    /**
     * Creates a database driver from assets.
     * Copies the database from assets to the app's database directory.
     */
    @Deprecated(
        message = "Use createDriver(filePath: String) instead to open databases directly without copying",
        replaceWith = ReplaceWith("createDriver(filePath)"),
        level = DeprecationLevel.WARNING,
    )
    public actual fun createDriver(
        name: String?,
        replaceDatabase: Boolean,
        completionHandler: (Boolean) -> Unit,
    ): SqlDriver {
        val dbName = name!!
        val database = context.getDatabasePath(dbName)

        if (!database.exists() || replaceDatabase) {
            val inputStream = context.assets.open(dbName)
            val outputStream = FileOutputStream(database.absolutePath)

            inputStream.use { input: InputStream ->
                outputStream.use { output: FileOutputStream ->
                    input.copyTo(output)
                }
            }

            completionHandler(true)
        } else {
            completionHandler(false)
        }

        return LogSqliteDriver(
            sqlDriver =
                AndroidSqliteDriver(
                    schema = BibleDatabase.Schema,
                    context = context,
                    name = dbName,
                    factory = OsmerionSQLiteOpenHelperFactory(),
                ),
        ) { text ->
            logBlock?.let { it(text) }
        }
    }

    /**
     * Creates a database driver from a direct file path.
     * Opens the database directly without copying.
     */
    public actual fun createDriver(filePath: String): SqlDriver {
        // Create a custom context that returns the full path for getDatabasePath
        val customContext = CustomDatabasePathContext(context, filePath)

        // Extract just the filename from the path for AndroidSqliteDriver
        val fileName = File(filePath).name

        return LogSqliteDriver(
            sqlDriver =
                AndroidSqliteDriver(
                    schema = BibleDatabase.Schema,
                    context = customContext,
                    name = fileName,
                    factory = OsmerionSQLiteOpenHelperFactory(),
                ),
        ) { text ->
            logBlock?.let { it(text) }
        }
    }
}

/**
 * Custom ContextWrapper that allows specifying a custom database path.
 * This is used to open databases from absolute file paths.
 */
private class CustomDatabasePathContext(
    base: Context,
    private val databasePath: String,
) : ContextWrapper(base) {
    override fun getDatabasePath(name: String): File = File(databasePath)
}

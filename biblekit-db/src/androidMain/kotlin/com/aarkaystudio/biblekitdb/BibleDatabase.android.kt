package com.aarkaystudio.biblekitdb

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import java.io.FileOutputStream
import java.io.InputStream

public actual class DriverFactory(
    private val context: Context,
    private val logBlock: ((String) -> Unit)? = null,
) {
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
            sqlDriver = AndroidSqliteDriver(BibleDatabase.Schema, context, dbName),
        ) { text ->
            logBlock?.let { it(text) }
        }
    }
}

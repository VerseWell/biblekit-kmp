package com.aarkaystudio.biblekitdb

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Platform-specific factory for creating SQLite database drivers.
 * Each platform (Android, iOS, JVM) provides its own implementation.
 */
public expect class DriverFactory {
    /**
     * Creates a platform-specific SQLite database driver.
     *
     * @param name The name of the database file
     * @param replaceDatabase Whether to replace the existing database with a new one
     * @param completionHandler Callback to handle completion of driver creation
     * @return A platform-specific [SqlDriver] implementation
     */
    public fun createDriver(
        name: String?,
        replaceDatabase: Boolean,
        completionHandler: (Boolean) -> Unit,
    ): SqlDriver
}

/**
 * Creates and initializes the Bible database.
 *
 * @param driverFactory Platform-specific factory for creating the database driver
 * @param replaceDatabase Whether to replace the existing database with a new one
 * @param completionHandler Callback that receives the new database version after successful creation
 * @return Initialized [BibleDatabase] instance
 */
public fun createDatabase(
    driverFactory: DriverFactory,
    replaceDatabase: Boolean,
    completionHandler: () -> Unit,
): BibleDatabase {
    val driver =
        driverFactory.createDriver(
            name = "bible.db",
            replaceDatabase = replaceDatabase,
            completionHandler = {
                if (it) {
                    completionHandler()
                }
            },
        )
    val database = BibleDatabase(driver)
    return database
}

/**
 * Extension function to execute a SQLDelight query asynchronously and return results as a list.
 * Supports cancellation and handles errors appropriately.
 *
 * @return List of query results of type [RowType]
 * @throws CancellationException if the query is interrupted
 * @throws Exception if any database error occurs
 */
internal suspend fun <RowType : Any> ExecutableQuery<RowType>.asyncExecuteAsList(): List<RowType> =
    suspendCancellableCoroutine { continuation ->
        try {
            val result =
                execute { cursor ->
                    val result = mutableListOf<RowType>()
                    while (cursor.next().value) {
                        // Check if the coroutine is still active before processing each row
                        if (!continuation.context.isActive) {
                            throw CancellationException("The Query was interrupted.")
                        }

                        val row = mapper(cursor)
                        result.add(row)
                    }
                    QueryResult.Value(result.toList())
                }.value

            continuation.resume(result)
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }

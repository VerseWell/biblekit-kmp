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
     * Creates a platform-specific SQLite database driver from assets/bundle.
     * The database file will be copied from assets/bundle to a writable location.
     *
     * @param name The name of the database file in assets/bundle
     * @param replaceDatabase Whether to replace the existing database with a new one
     * @param completionHandler Callback to handle completion of driver creation
     * @return A platform-specific [SqlDriver] implementation
     */
    @Deprecated(
        message = "Use createDriver(filePath: String) instead to open databases directly without copying",
        replaceWith = ReplaceWith("createDriver(filePath)"),
        level = DeprecationLevel.WARNING,
    )
    public fun createDriver(
        name: String?,
        replaceDatabase: Boolean,
        completionHandler: (Boolean) -> Unit,
    ): SqlDriver

    /**
     * Creates a platform-specific SQLite database driver from a direct file path.
     * No copying occurs - the database is opened directly from the provided path in read-only mode.
     *
     * @param filePath The absolute file path to the database file
     * @return A platform-specific [SqlDriver] implementation
     */
    public fun createDriver(filePath: String): SqlDriver
}

/**
 * Creates and initializes the Bible database from assets/bundle.
 *
 * @param driverFactory Platform-specific factory for creating the database driver
 * @param replaceDatabase Whether to replace the existing database with a new one
 * @param completionHandler Callback that receives the new database version after successful creation
 * @return Initialized [BibleDatabase] instance
 */
@Deprecated(
    message = "Use createDatabase(filePath, driverFactory) instead to open databases directly without copying",
    replaceWith = ReplaceWith("createDatabase(filePath, driverFactory)"),
    level = DeprecationLevel.WARNING,
)
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
 * Creates and initializes the Bible database from a direct file path.
 * No copying occurs - the database is opened directly from the provided path.
 *
 * @param filePath The absolute file path to the database file
 * @param driverFactory Platform-specific factory for creating the database driver
 * @return Initialized [BibleDatabase] instance
 */
public fun createDatabase(
    filePath: String,
    driverFactory: DriverFactory,
): BibleDatabase {
    val driver = driverFactory.createDriver(filePath = filePath)
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

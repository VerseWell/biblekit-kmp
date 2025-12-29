package com.aarkaystudio.biblekitdb

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringByAppendingPathComponent

public actual class DriverFactory(
    private val logBlock: ((String) -> Unit)? = null,
) {
    /**
     * Creates a database driver from bundle.
     * Copies the database from bundle to Application Support directory.
     */
    @Deprecated(
        message = "Use createDriver(filePath: String) instead to open databases directly without copying",
        replaceWith = ReplaceWith("createDriver(filePath)"),
        level = DeprecationLevel.WARNING,
    )
    @OptIn(ExperimentalForeignApi::class)
    public actual fun createDriver(
        name: String?,
        replaceDatabase: Boolean,
        completionHandler: (Boolean) -> Unit,
    ): SqlDriver =
        if (name != null) {
            val fileManager = NSFileManager.defaultManager
            val documentsPath =
                NSSearchPathForDirectoriesInDomains(
                    directory = NSApplicationSupportDirectory,
                    domainMask = NSUserDomainMask,
                    expandTilde = true,
                ).first() as NSString

            val dbDirectoryPath = documentsPath.stringByAppendingPathComponent("databases")
            val targetDbPath = documentsPath.stringByAppendingPathComponent("databases/$name")
            val sourceDbPath = NSBundle.mainBundle.pathForResource(name, null)!!

            if (replaceDatabase) {
                memScoped {
                    val error: ObjCObjectVar<NSError?> = alloc()
                    fileManager.removeItemAtPath(path = dbDirectoryPath, error = error.ptr)
                }
            }

            val directoryExists = fileManager.fileExistsAtPath(dbDirectoryPath)
            val databaseExists = fileManager.fileExistsAtPath(targetDbPath)

            if (databaseExists.not()) {
                memScoped {
                    val dirError: ObjCObjectVar<NSError?> = alloc()

                    fileManager.createDirectoryAtPath(
                        path = dbDirectoryPath,
                        withIntermediateDirectories = true,
                        attributes = null,
                        error = dirError.ptr,
                    )

                    if (dirError.value != null) {
                        throw IllegalStateException(dirError.value!!.localizedDescription())
                    }

                    val fileError: ObjCObjectVar<NSError?> = alloc()

                    fileManager.copyItemAtPath(
                        srcPath = sourceDbPath,
                        toPath = targetDbPath,
                        error = fileError.ptr,
                    )

                    if (fileError.value != null) {
                        throw IllegalStateException(fileError.value!!.localizedDescription())
                    }
                }

                completionHandler(true)
            } else {
                completionHandler(false)
            }

            LogSqliteDriver(
                sqlDriver =
                    NativeSqliteDriver(
                        schema = BibleDatabase.Schema,
                        name = name,
                    ),
            ) { text ->
                logBlock?.let { it(text) }
            }
        } else {
            completionHandler(false)

            LogSqliteDriver(
                sqlDriver =
                    NativeSqliteDriver(
                        DatabaseConfiguration(
                            name = null,
                            version = BibleDatabase.Schema.version.toInt(),
                            create = { con -> wrapConnection(con) { BibleDatabase.Schema.create(it) } },
                            inMemory = true,
                        ),
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
        // Split the file path into directory and filename
        // DatabaseConfiguration.name cannot contain path separators, so we use basePath for the directory
        val lastSeparator = filePath.lastIndexOf('/')
        val (directory, filename) =
            if (lastSeparator != -1) {
                filePath.substring(0, lastSeparator) to filePath.substring(lastSeparator + 1)
            } else {
                null to filePath
            }

        return LogSqliteDriver(
            sqlDriver =
                NativeSqliteDriver(
                    DatabaseConfiguration(
                        name = filename,
                        version = BibleDatabase.Schema.version.toInt(),
                        create = { con -> wrapConnection(con) { BibleDatabase.Schema.create(it) } },
                        inMemory = false,
                        extendedConfig =
                            DatabaseConfiguration.Extended(
                                basePath = directory,
                            ),
                    ),
                ),
        ) { text ->
            logBlock?.let { it(text) }
        }
    }
}

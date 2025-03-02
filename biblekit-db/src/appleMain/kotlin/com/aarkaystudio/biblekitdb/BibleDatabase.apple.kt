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
    @OptIn(ExperimentalForeignApi::class)
    public actual fun createDriver(
        name: String?,
        replaceDatabase: Boolean,
        completionHandler: (Boolean) -> Unit,
    ): SqlDriver {
        return if (name != null) {
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

            return LogSqliteDriver(
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
    }
}

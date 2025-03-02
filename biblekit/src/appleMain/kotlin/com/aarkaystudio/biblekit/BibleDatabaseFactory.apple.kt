package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekitdb.BibleDatabase
import com.aarkaystudio.biblekitdb.DriverFactory
import com.aarkaystudio.biblekitdb.createDatabase

public actual class BibleDatabaseFactory(
    private val replaceDatabase: Boolean,
    private val completionHandler: () -> Unit,
) {
    public actual fun create(): BibleDatabase =
        createDatabase(
            driverFactory = DriverFactory(),
            replaceDatabase = replaceDatabase,
            completionHandler = completionHandler,
        )
}

package com.aarkaystudio.biblekit

import android.content.Context
import com.aarkaystudio.biblekitdb.BibleDatabase
import com.aarkaystudio.biblekitdb.DriverFactory
import com.aarkaystudio.biblekitdb.createDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class BibleDatabaseFactory(
    private val context: Context,
    private val replaceDatabase: Boolean,
    private val completionHandler: () -> Unit,
) {
    public actual fun create(): BibleDatabase =
        createDatabase(
            driverFactory = DriverFactory(context = context),
            replaceDatabase = replaceDatabase,
            completionHandler = completionHandler,
        )
}

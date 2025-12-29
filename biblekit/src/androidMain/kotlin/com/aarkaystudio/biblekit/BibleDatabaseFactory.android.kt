package com.aarkaystudio.biblekit

import android.content.Context
import com.aarkaystudio.biblekitdb.BibleDatabase
import com.aarkaystudio.biblekitdb.DriverFactory
import com.aarkaystudio.biblekitdb.createDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class BibleDatabaseFactory
    @Deprecated(
        message = "Use BibleDatabaseFactory(context) and create(filePath) instead to open databases directly without copying",
        replaceWith = ReplaceWith("BibleDatabaseFactory(context)"),
        level = DeprecationLevel.WARNING,
    )
    constructor(
        private val context: Context,
        private val replaceDatabase: Boolean,
        private val completionHandler: () -> Unit,
    ) {
        /**
         * Constructor for creating a factory that opens databases from file paths.
         *
         * @param context The Android application context
         */
        public constructor(context: Context) : this(context, false, {})

        @Deprecated(
            message = "Use create(filePath) instead to open databases directly without copying",
            replaceWith = ReplaceWith("create(filePath)"),
            level = DeprecationLevel.WARNING,
        )
        public actual fun create(): BibleDatabase =
            createDatabase(
                driverFactory = DriverFactory(context = context),
                replaceDatabase = replaceDatabase,
                completionHandler = completionHandler,
            )

        /**
         * Creates a database using a direct file path (no asset copying).
         *
         * @param filePath The absolute file path to the database file
         * @return Initialized [BibleDatabase] instance
         */
        public actual fun create(filePath: String): BibleDatabase =
            createDatabase(
                filePath = filePath,
                driverFactory = DriverFactory(context = context),
            )
    }

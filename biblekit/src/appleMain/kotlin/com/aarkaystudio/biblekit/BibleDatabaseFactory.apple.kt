package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekitdb.BibleDatabase
import com.aarkaystudio.biblekitdb.DriverFactory
import com.aarkaystudio.biblekitdb.createDatabase

public actual class BibleDatabaseFactory
    @Deprecated(
        message = "Use BibleDatabaseFactory() and create(filePath) instead to open databases directly without copying",
        replaceWith = ReplaceWith("BibleDatabaseFactory()"),
        level = DeprecationLevel.WARNING,
    )
    constructor(
        private val replaceDatabase: Boolean,
        private val completionHandler: () -> Unit,
    ) {
        /**
         * Constructor for creating a factory that opens databases from file paths.
         */
        public constructor() : this(false, {})

        @Deprecated(
            message = "Use create(filePath) instead to open databases directly without copying",
            replaceWith = ReplaceWith("create(filePath)"),
            level = DeprecationLevel.WARNING,
        )
        public actual fun create(): BibleDatabase =
            createDatabase(
                driverFactory = DriverFactory(),
                replaceDatabase = replaceDatabase,
                completionHandler = completionHandler,
            )

        /**
         * Creates a database using a direct file path.
         *
         * @param filePath The absolute file path to the database file
         * @return Initialized [BibleDatabase] instance
         */
        public actual fun create(filePath: String): BibleDatabase =
            createDatabase(
                filePath = filePath,
                driverFactory = DriverFactory(),
            )
    }

package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekitdb.BibleDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public expect class BibleDatabaseFactory {
    @Deprecated(
        message = "Use create(filePath) instead to open databases directly without copying",
        replaceWith = ReplaceWith("create(filePath)"),
        level = DeprecationLevel.WARNING,
    )
    internal fun create(): BibleDatabase

    internal fun create(filePath: String): BibleDatabase
}

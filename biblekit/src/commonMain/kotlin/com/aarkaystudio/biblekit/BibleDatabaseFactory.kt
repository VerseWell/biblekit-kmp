package com.aarkaystudio.biblekit

import com.aarkaystudio.biblekitdb.BibleDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public expect class BibleDatabaseFactory {
    internal fun create(): BibleDatabase
}

package com.aarkaystudio.biblekitdb

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.aarkaystudio.biblekitdb.BibleDatabase

internal actual fun testDbConnection(): SqlDriver =
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        .also { BibleDatabase.Schema.create(it) }

package com.aarkaystudio.biblekitdb

import app.cash.sqldelight.db.SqlDriver

internal actual fun testDbConnection(): SqlDriver =
    DriverFactory().createDriver(
        name = null,
        replaceDatabase = false,
        completionHandler = {},
    )

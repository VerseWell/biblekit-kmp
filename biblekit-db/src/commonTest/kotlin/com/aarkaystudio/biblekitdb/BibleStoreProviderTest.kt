package com.aarkaystudio.biblekitdb

import app.cash.sqldelight.db.SqlDriver

internal expect fun testDbConnection(): SqlDriver

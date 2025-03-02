/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aarkaystudio.biblekitdb

import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement

/**
 * A SQLite driver implementation that logs all database operations.
 * This driver wraps another SQLite driver and adds logging capabilities
 * for debugging and monitoring purposes.
 *
 * @property sqlDriver The underlying SQLite driver that performs the actual database operations
 * @property logger A function that handles the logging of database operations
 */
internal class LogSqliteDriver(
    private val sqlDriver: SqlDriver,
    private val logger: (String) -> Unit,
) : SqlDriver {
    /**
     * Executes a SQL statement that doesn't return a result set.
     *
     * @param identifier Optional query identifier
     * @param sql The SQL statement to execute
     * @param parameters Number of parameters in the statement
     * @param binders Optional function to bind parameters
     * @return Query result containing the number of affected rows
     */
    override fun execute(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?,
    ): QueryResult<Long> {
        logger("EXECUTE\n$sql")
        logParameters(binders)
        val result =
            sqlDriver.execute(
                identifier = identifier,
                sql = sql,
                parameters = parameters,
                binders = binders,
            )
        logger("QUERY_RESULT\n$result")
        return result
    }

    /**
     * Executes a SQL query that returns a result set.
     *
     * @param identifier Optional query identifier
     * @param sql The SQL query to execute
     * @param mapper Function to map the result set to the desired type
     * @param parameters Number of parameters in the query
     * @param binders Optional function to bind parameters
     * @return Query result containing the mapped data
     */
    override fun <R> executeQuery(
        identifier: Int?,
        sql: String,
        mapper: (SqlCursor) -> QueryResult<R>,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?,
    ): QueryResult<R> {
        logger("QUERY\n$sql")
        logParameters(binders)
        val result =
            sqlDriver.executeQuery(
                identifier = identifier,
                sql = sql,
                mapper = mapper,
                parameters = parameters,
                binders = binders,
            )
        logger("QUERY_RESULT\n$result")
        return result
    }

    /**
     * Returns the current transaction if one exists.
     *
     * @return The current transaction or null if none exists
     */
    override fun currentTransaction(): Transacter.Transaction? = sqlDriver.currentTransaction()

    /**
     * Starts a new database transaction.
     *
     * @return Query result containing the new transaction
     */
    override fun newTransaction(): QueryResult<Transacter.Transaction> {
        logger("TRANSACTION BEGIN")
        val transaction = sqlDriver.newTransaction()
        transaction.value.afterCommit { logger("TRANSACTION COMMIT") }
        transaction.value.afterRollback { logger("TRANSACTION ROLLBACK") }
        return transaction
    }

    /**
     * Adds a listener for the specified query keys.
     *
     * @param queryKeys The keys to listen for changes
     * @param listener The listener to be notified of changes
     */
    override fun addListener(
        vararg queryKeys: String,
        listener: Query.Listener,
    ) {
        logger("addListener $queryKeys")
        sqlDriver.addListener(queryKeys = queryKeys, listener = listener)
    }

    /**
     * Notifies listeners for the specified query keys.
     *
     * @param queryKeys The keys whose listeners should be notified
     */
    override fun notifyListeners(vararg queryKeys: String) {
        logger("notifyListeners $queryKeys")
        sqlDriver.notifyListeners(queryKeys = queryKeys)
    }

    /**
     * Removes a listener for the specified query keys.
     *
     * @param queryKeys The keys to stop listening for
     * @param listener The listener to remove
     */
    override fun removeListener(
        vararg queryKeys: String,
        listener: Query.Listener,
    ) {
        logger("removeListener $queryKeys")
        sqlDriver.removeListener(queryKeys = queryKeys, listener = listener)
    }

    /**
     * Closes the database connection.
     */
    override fun close() {
        logger("CLOSE CONNECTION")
        sqlDriver.close()
    }

    /**
     * Logs the parameters that will be bound to a prepared statement.
     *
     * @param binders The function containing parameter bindings
     */
    private fun logParameters(binders: (SqlPreparedStatement.() -> Unit)?) {
        binders?.let { func ->
            val parametersInterceptor = StatementParameterInterceptor()
            parametersInterceptor.func()
            val logParameters = parametersInterceptor.getAndClearParameters()
            if (logParameters.isNotEmpty()) logger(" $logParameters")
        }
    }
}

/**
 * Helper class that intercepts and collects parameter bindings for logging purposes.
 * Implements SqlPreparedStatement to capture all types of parameter bindings.
 */
private class StatementParameterInterceptor : SqlPreparedStatement {
    private val values = mutableListOf<Any?>()

    override fun bindBoolean(
        index: Int,
        boolean: Boolean?,
    ) {
        values.add(boolean)
    }

    override fun bindBytes(
        index: Int,
        bytes: ByteArray?,
    ) {
        values.add(bytes)
    }

    override fun bindDouble(
        index: Int,
        double: Double?,
    ) {
        values.add(double)
    }

    override fun bindLong(
        index: Int,
        long: Long?,
    ) {
        values.add(long)
    }

    override fun bindString(
        index: Int,
        string: String?,
    ) {
        values.add(string)
    }

    /**
     * Returns and clears the collected parameter values.
     *
     * @return List of parameter values that were bound
     */
    fun getAndClearParameters(): List<Any?> {
        val list = values.toList()
        values.clear()
        return list
    }
}

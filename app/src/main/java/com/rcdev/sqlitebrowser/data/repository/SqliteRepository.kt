package com.rcdev.sqlitebrowser.data.repository

import com.rcdev.sqlitebrowser.domain.model.TableInfo
import kotlinx.coroutines.flow.Flow

interface SqliteRepository {
    suspend fun openDatabase(dbPath: String, password: String?): Result<Unit>
    suspend fun getTables(): Result<List<String>>
    suspend fun getTableInfo(tableName: String): Result<TableInfo>
    suspend fun executeQuery(query: String): Result<QueryResultData>
    suspend fun executeNonQuery(sql: String): Result<Unit>
    suspend fun updateRecord(tableName: String, primaryKeyColumn: String, primaryKeyValue: Any, values: Map<String, Any?>): Result<Unit>
    fun closeDatabase()
}

data class QueryResultData(
    val columns: List<String>,
    val rows: List<List<Any?>>
)

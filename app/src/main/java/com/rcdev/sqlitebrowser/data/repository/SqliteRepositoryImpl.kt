package com.rcdev.sqlitebrowser.data.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.rcdev.sqlitebrowser.domain.model.TableColumn
import com.rcdev.sqlitebrowser.domain.model.TableInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper
import java.io.File
import javax.inject.Inject

class SqliteRepositoryImpl @Inject constructor(
    private val context: Context
) : SqliteRepository {

    private var database: SQLiteDatabase? = null

    override suspend fun openDatabase(dbPath: String, password: String?): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            closeDatabase()
            SQLiteDatabase.loadLibs(context)
            
            val dbFile = File(dbPath)
            if (!dbFile.exists()) return@withContext Result.failure(Exception("Archivo no encontrado"))

            database = SQLiteDatabase.openDatabase(
                dbPath,
                password ?: "",
                null,
                SQLiteDatabase.OPEN_READWRITE
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTables(): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val db = database ?: return@withContext Result.failure(Exception("Base de datos no abierta"))
            val tables = mutableListOf<String>()
            val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
            
            cursor?.use {
                while (it.moveToNext()) {
                    tables.add(it.getString(0))
                }
            }
            Result.success(tables)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTableInfo(tableName: String): Result<TableInfo> = withContext(Dispatchers.IO) {
        try {
            val db = database ?: return@withContext Result.failure(Exception("Base de datos no abierta"))
            val columns = mutableListOf<TableColumn>()
            val cursor = db.rawQuery("PRAGMA table_info($tableName)", null)
            
            cursor?.use {
                val nameIndex = it.getColumnIndex("name")
                val typeIndex = it.getColumnIndex("type")
                val pkIndex = it.getColumnIndex("pk")
                val notNullIndex = it.getColumnIndex("notnull")

                while (it.moveToNext()) {
                    columns.add(
                        TableColumn(
                            columnName = it.getString(nameIndex),
                            dataType = it.getString(typeIndex),
                            isPK = it.getInt(pkIndex) == 1,
                            allowNull = it.getInt(notNullIndex) == 0
                        )
                    )
                }
            }
            Result.success(TableInfo(tableName, columns))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun executeQuery(query: String): Result<QueryResultData> = withContext(Dispatchers.IO) {
        try {
            val db = database ?: return@withContext Result.failure(Exception("Base de datos no abierta"))
            val cursor = db.rawQuery(query, null)
            
            cursor?.use {
                val columnNames = it.columnNames.toList()
                val rows = mutableListOf<List<Any?>>()
                
                while (it.moveToNext()) {
                    val row = mutableListOf<Any?>()
                    for (i in 0 until it.columnCount) {
                        row.add(when (it.getType(i)) {
                            Cursor.FIELD_TYPE_STRING -> it.getString(i)
                            Cursor.FIELD_TYPE_INTEGER -> it.getLong(i)
                            Cursor.FIELD_TYPE_FLOAT -> it.getDouble(i)
                            Cursor.FIELD_TYPE_BLOB -> "[BLOB]"
                            else -> null
                        })
                    }
                    rows.add(row)
                }
                Result.success(QueryResultData(columnNames, rows))
            } ?: Result.failure(Exception("Error al ejecutar query"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun executeNonQuery(sql: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = database ?: return@withContext Result.failure(Exception("Base de datos no abierta"))
            db.execSQL(sql)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateRecord(
        tableName: String,
        primaryKeyColumn: String,
        primaryKeyValue: Any,
        values: Map<String, Any?>
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = database ?: return@withContext Result.failure(Exception("Base de datos no abierta"))
            val contentValues = android.content.ContentValues()
            values.forEach { (key, value) ->
                when (value) {
                    is String -> contentValues.put(key, value)
                    is Int -> contentValues.put(key, value)
                    is Long -> contentValues.put(key, value)
                    is Double -> contentValues.put(key, value)
                    is Float -> contentValues.put(key, value)
                    is Boolean -> contentValues.put(key, value)
                    null -> contentValues.putNull(key)
                }
            }
            
            val affectedRows = db.update(
                tableName,
                contentValues,
                "$primaryKeyColumn = ?",
                arrayOf(primaryKeyValue.toString())
            )
            
            if (affectedRows > 0) Result.success(Unit)
            else Result.failure(Exception("No se actualizó ninguna fila"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun closeDatabase() {
        database?.close()
        database = null
    }
}

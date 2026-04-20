package com.rcdev.sqlitebrowser.domain.model

data class TableInfo(
    val tableName: String,
    val columns: List<TableColumn>
)

data class TableColumn(
    val columnName: String,
    val dataType: String,
    val isPK: Boolean,
    val allowNull: Boolean
)

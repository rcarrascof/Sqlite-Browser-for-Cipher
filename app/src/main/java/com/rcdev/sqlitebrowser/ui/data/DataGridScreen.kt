package com.rcdev.sqlitebrowser.ui.data

import com.rcdev.sqlitebrowser.data.repository.QueryResultData
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataGridScreen(
    viewModel: DataViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val horizontalScrollState = rememberScrollState()
    var selectedRowForEdit by remember { mutableStateOf<List<Any?>?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Visualizador de Datos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is DataUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DataUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(24.dp)
                    )
                }
                is DataUiState.Success -> {
                    DataGrid(
                        columns = state.data.columns,
                        rows = state.data.rows,
                        scrollState = horizontalScrollState,
                        onRowLongClick = { selectedRowForEdit = it }
                    )
                }
            }
        }
    }

    if (selectedRowForEdit != null && uiState is DataUiState.Success) {
        val state = uiState as DataUiState.Success
        EditRowDialog(
            columns = state.data.columns,
            rowValues = selectedRowForEdit!!,
            onDismiss = { selectedRowForEdit = null },
            onSave = { updatedValues ->
                // Por simplicidad, usamos la primera columna como PK para este ejemplo
                // En un caso real, buscaríamos la PK real de TableInfo
                viewModel.updateRecord(state.data.columns[0], selectedRowForEdit!![0]!!, updatedValues)
                selectedRowForEdit = null
            }
        )
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun DataGrid(
    columns: List<String>,
    rows: List<List<Any?>>,
    scrollState: ScrollState,
    onRowLongClick: (List<Any?>) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            columns.forEach { column ->
                Surface(
                    modifier = Modifier.width(150.dp).padding(4.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = column,
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(rows) { row ->
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .combinedClickable(
                            onClick = {},
                            onLongClick = { onRowLongClick(row) }
                        )
                ) {
                    row.forEach { cell ->
                        Box(
                            modifier = Modifier.width(150.dp).padding(4.dp).heightIn(min = 48.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = cell?.toString() ?: "NULL",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun EditRowDialog(
    columns: List<String>,
    rowValues: List<Any?>,
    onDismiss: () -> Unit,
    onSave: (Map<String, Any?>) -> Unit
) {
    val editedValues = remember { 
        mutableStateMapOf<String, String>().apply {
            columns.forEachIndexed { index, col ->
                put(col, rowValues[index]?.toString() ?: "")
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Registro") },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(columns) { column ->
                    TextField(
                        value = editedValues[column] ?: "",
                        onValueChange = { editedValues[column] = it },
                        label = { Text(column) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(editedValues.toMap()) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

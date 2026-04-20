package com.rcdev.sqlitebrowser.ui.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rcdev.sqlitebrowser.data.repository.QueryResultData
import com.rcdev.sqlitebrowser.data.repository.SqliteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: SqliteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tableName: String = checkNotNull(savedStateHandle["tableName"])

    private val _uiState = MutableStateFlow<DataUiState>(DataUiState.Loading)
    val uiState: StateFlow<DataUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _uiState.value = DataUiState.Loading
        viewModelScope.launch {
            val result = repository.executeQuery("SELECT * FROM $tableName")
            if (result.isSuccess) {
                val data = result.getOrNull()
                if (data != null) {
                    _uiState.value = DataUiState.Success(data)
                } else {
                    _uiState.value = DataUiState.Error("No se pudieron cargar los datos")
                }
            } else {
                _uiState.value = DataUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun updateRecord(primaryKeyColumn: String, primaryKeyValue: Any, updatedValues: Map<String, Any?>) {
        viewModelScope.launch {
            repository.updateRecord(tableName, primaryKeyColumn, primaryKeyValue, updatedValues)
                .onSuccess {
                    loadData() // Recargar datos tras actualizar
                }
                .onFailure { e ->
                    _uiState.value = DataUiState.Error(e.message ?: "Error al actualizar registro")
                }
        }
    }
}

sealed class DataUiState {
    object Loading : DataUiState()
    data class Success(val data: QueryResultData) : DataUiState()
    data class Error(val message: String) : DataUiState()
}

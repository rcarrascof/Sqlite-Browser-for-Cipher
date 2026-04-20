package com.rcdev.sqlitebrowser.ui.query

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rcdev.sqlitebrowser.data.repository.SqliteRepository
import com.rcdev.sqlitebrowser.data.repository.QueryResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class QueryUiState {
    object Idle : QueryUiState()
    object Loading : QueryUiState()
    data class Success(val data: QueryResultData?, val message: String? = null) : QueryUiState()
    data class Error(val message: String) : QueryUiState()
}

@HiltViewModel
class QueryViewModel @Inject constructor(
    private val repository: SqliteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QueryUiState>(QueryUiState.Idle)
    val uiState: StateFlow<QueryUiState> = _uiState

    fun executeQuery(sql: String) {
        viewModelScope.launch {
            _uiState.value = QueryUiState.Loading
            
            val cleanSql = sql.trim().uppercase()
            if (cleanSql.startsWith("SELECT") || cleanSql.startsWith("PRAGMA")) {
                repository.executeQuery(sql).onSuccess { data ->
                    _uiState.value = QueryUiState.Success(data)
                }.onFailure { e ->
                    _uiState.value = QueryUiState.Error(e.message ?: "Error desconocido")
                }
            } else {
                repository.executeNonQuery(sql).onSuccess {
                    _uiState.value = QueryUiState.Success(null, "Comando ejecutado con éxito")
                }.onFailure { e ->
                    _uiState.value = QueryUiState.Error(e.message ?: "Error al ejecutar comando")
                }
            }
        }
    }
}

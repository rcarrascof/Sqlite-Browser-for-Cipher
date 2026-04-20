package com.rcdev.sqlitebrowser.ui.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rcdev.sqlitebrowser.data.repository.SqliteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TablesViewModel @Inject constructor(
    private val repository: SqliteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TablesUiState>(TablesUiState.Loading)
    val uiState: StateFlow<TablesUiState> = _uiState.asStateFlow()

    init {
        loadTables()
    }

    fun loadTables() {
        _uiState.value = TablesUiState.Loading
        viewModelScope.launch {
            val result = repository.getTables()
            if (result.isSuccess) {
                _uiState.value = TablesUiState.Success(result.getOrDefault(emptyList()))
            } else {
                _uiState.value = TablesUiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar tablas")
            }
        }
    }
}

sealed class TablesUiState {
    object Loading : TablesUiState()
    data class Success(val tables: List<String>) : TablesUiState()
    data class Error(val message: String) : TablesUiState()
}

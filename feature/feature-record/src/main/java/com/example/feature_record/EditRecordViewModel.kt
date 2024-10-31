package com.example.feature_record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RecordRepository
import com.example.model.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecordViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val id: Long =
        savedStateHandle.get<Long>("recordId") ?: throw IllegalArgumentException("id is required")

    sealed interface UiState {
        data object Initial : UiState
        data object Loading : UiState
        data class Idle(val record: Record) : UiState
        data class LoadSuccess(val record: Record) : UiState
        data class LoadError(val error: Exception) : UiState
        data class ConfirmDelete(val record: Record) : UiState
        data object DeleteSuccess : UiState
        data class DeleteError(val record: Record, val e: Exception) : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun moveToIdle() {
        val currentState = _uiState.value
        if (currentState is UiState.LoadSuccess) {
            _uiState.value = UiState.Idle(currentState.record)
        } else if (currentState is UiState.ConfirmDelete) {
            _uiState.value = UiState.Idle(currentState.record)
        } else if (currentState is UiState.DeleteError) {
            _uiState.value = UiState.Idle(currentState.record)
        }

    }

    fun load() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val record = recordRepository.getById(id)
                if (record == null) {
                    _uiState.value = UiState.LoadError(IllegalArgumentException("Record not found"))
                    return@launch
                }
                _uiState.value = UiState.LoadSuccess(record)
            } catch (e: Exception) {
                _uiState.value = UiState.LoadError(e)
            }
        }
    }

    fun showDeleteDialog() {
        val currentState = _uiState.value
        if (currentState !is UiState.Idle) {
            return
        }

        _uiState.value = UiState.ConfirmDelete(currentState.record)
    }

    fun delete() {
        val currentState = _uiState.value
        if (currentState !is UiState.ConfirmDelete) {
            return
        }

        viewModelScope.launch {
            try {
                recordRepository.delete(currentState.record)
                _uiState.value = UiState.DeleteSuccess
            } catch (e: Exception) {
                _uiState.value = UiState.DeleteError(currentState.record, e)
            }
        }
    }

}
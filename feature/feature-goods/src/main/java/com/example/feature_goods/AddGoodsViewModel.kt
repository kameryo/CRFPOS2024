package com.example.feature_goods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.GoodsRepository
import com.example.model.Goods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGoodsViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository,
) : ViewModel() {
    sealed interface UiState {
        data object Idle : UiState
        data object InputError : UiState
        data object Success : UiState
        data class AddError(val e: Exception) : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun add(goods: Goods) {
        viewModelScope.launch {
            try {
                goodsRepository.add(goods)
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.AddError(e)
            }
        }
    }

    fun moveToIdle() {
        _uiState.value = UiState.Idle
    }
}
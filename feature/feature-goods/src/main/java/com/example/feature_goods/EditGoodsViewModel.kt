package com.example.feature_goods

import androidx.lifecycle.SavedStateHandle
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
class EditGoodsViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val id: Long =
        savedStateHandle.get<Long>("goodsId") ?: throw IllegalArgumentException("id is required")

    sealed interface UiState {
        data object Initial : UiState
        data object Loading : UiState
        data class LoadSuccess(val goods: Goods) : UiState
        data class LoadError(val error: Throwable) : UiState
        data class Idle(val goods: Goods) : UiState
        data class InputError(val goods: Goods) : UiState
        data object UpdateInProgress : UiState
        data object UpdateSuccess : UiState
        data class UpdateError(val goods: Goods, val e: Exception) : UiState
        data class ConfirmDelete(val goods: Goods) : UiState

        data object DeleteInProgress : UiState
        data object DeleteSuccess : UiState
        data class DeleteError(val goods: Goods, val e: Exception) : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun moveToIdle() {
        val currentState = _uiState.value
        if (currentState is UiState.LoadSuccess) {
            _uiState.value = UiState.Idle(currentState.goods)
        } else if (currentState is UiState.InputError) {
            _uiState.value = UiState.Idle(currentState.goods)
        } else if (currentState is UiState.UpdateError) {
            _uiState.value = UiState.Idle(currentState.goods)
        } else if (currentState is UiState.ConfirmDelete) {
            _uiState.value = UiState.Idle(currentState.goods)
        } else if (currentState is UiState.DeleteError) {
            _uiState.value = UiState.Idle(currentState.goods)
        }

    }

    fun load() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val goods = goodsRepository.getById(id)
                if (goods == null) {
                    _uiState.value = UiState.LoadError(IllegalArgumentException("id is not found"))
                    return@launch
                }
                _uiState.value = UiState.LoadSuccess(goods)
            } catch (e: Exception) {
                _uiState.value = UiState.LoadError(e)

            }
        }
    }

    fun update(name: String, price: Long) {
        val currentState = _uiState.value
        if (currentState !is UiState.Idle) {
            return
        }

        if (name.isEmpty()) {
            _uiState.value = UiState.InputError(currentState.goods)
            return
        }
        _uiState.value = UiState.UpdateInProgress
        viewModelScope.launch {
            try {
                goodsRepository.update(
                    currentState.goods.copy(

                        name = name,
                        price = price,
                    )
                )
                _uiState.value = UiState.UpdateSuccess
            } catch (e: Exception) {
                _uiState.value = UiState.UpdateError(currentState.goods, e)
            }
        }
    }

    fun showDeleteDialog() {
        val currentState = _uiState.value
        if (currentState !is UiState.Idle) {
            return
        }
        _uiState.value = UiState.ConfirmDelete(currentState.goods)
    }

    fun delete() {
        val currentState = _uiState.value
        if (currentState !is UiState.ConfirmDelete) {
            return
        }
        _uiState.value = UiState.DeleteInProgress
        viewModelScope.launch {
            try {
                goodsRepository.delete(currentState.goods)
                _uiState.value = UiState.DeleteSuccess
            } catch (e: Exception) {
                _uiState.value = UiState.DeleteError(currentState.goods, e)
            }
        }
    }

}

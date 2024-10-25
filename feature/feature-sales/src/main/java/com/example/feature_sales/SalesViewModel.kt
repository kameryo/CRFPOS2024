package com.example.feature_sales

import androidx.lifecycle.ViewModel
import com.example.data.repository.GoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SalesScreenState(
    val adultCount: Int,
    val childCount: Int,
    val adultManualCountText: String,
    val childManualCountText: String,
)


@HiltViewModel
class SalesViewModel @Inject constructor(
    private val goodsRepository: GoodsRepository,
) : ViewModel() {
    sealed interface UiState {
        data object Initial : UiState
        data object Loading : UiState
        data object Resetting : UiState
        data object ResetSuccess : UiState
        data object ResetError : UiState
        data class UpdatingScreen(val state: SalesScreenState) : UiState

        data object Idle : UiState

        data object UpdateInProgress : UiState
        data object UpdateSuccess : UiState

    }

    private val _salesScreenState = MutableStateFlow<SalesScreenState>(
        SalesScreenState(
            adultCount = 0,
            childCount = 0,
            adultManualCountText = "",
            childManualCountText = "",
        )
    )
    private val salesScreenState: StateFlow<SalesScreenState> = _salesScreenState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun moveToIdle() {
        _uiState.value = UiState.Idle
    }


    fun reset() {
        _uiState.value = UiState.Resetting
        _salesScreenState.value = SalesScreenState(
            adultCount = 0,
            childCount = 0,
            adultManualCountText = "",
            childManualCountText = "",
        )
        _uiState.value = UiState.ResetSuccess
        _uiState.value = UiState.UpdatingScreen(state = _salesScreenState.value)
    }

    fun updateAdultCount(adultCount: Int) {
        _uiState.value = UiState.UpdateInProgress
        _salesScreenState.value = _salesScreenState.value.copy(adultCount = adultCount)
        _uiState.value = UiState.UpdateSuccess
        _uiState.value = UiState.UpdatingScreen(state = _salesScreenState.value)
    }

    fun updateChildCount(childCount: Int) {
        _uiState.value = UiState.UpdateInProgress
        _salesScreenState.value = _salesScreenState.value.copy(childCount = childCount)
        _uiState.value = UiState.UpdateSuccess
        _uiState.value = UiState.UpdatingScreen(state = _salesScreenState.value)
    }

}
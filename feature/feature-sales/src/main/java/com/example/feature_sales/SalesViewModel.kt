package com.example.feature_sales

import androidx.lifecycle.ViewModel
import com.example.data.repository.GoodsRepository
import com.example.model.Calculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SalesScreenState(
    val adultCount: Int = 0,
    val childCount: Int = 0,
//    val selectedGoods: List<CartItem> = emptyList(),
    val subFare: Int = 0,
    val subGoods: Int = 0,
    val total: Int = 0,
    val normalTicketCount: Int = 0,
    val accompanyTicketCount: Int = 0,
    val drivingTicketCount: Int = 0,
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
//        data class UpdatingScreen(val state: SalesScreenState) : UiState

        data object Idle : UiState
        
        data object UpdateSuccess : UiState

    }

    val goodsItems = goodsRepository.getAll()

    private val calculator = Calculator()

    private val _salesScreenState = MutableStateFlow(
        SalesScreenState()
    )
    val salesScreenState: StateFlow<SalesScreenState> = _salesScreenState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun moveToIdle() {
        _uiState.value = UiState.Idle
    }


    fun reset() {
        _uiState.value = UiState.Resetting
        _salesScreenState.value = SalesScreenState()
        _uiState.value = UiState.ResetSuccess
    }

    fun updateAdultCount(adultCount: Int) {
        _salesScreenState.value = _salesScreenState.value.copy(adultCount = adultCount)
        calculate()
    }

    fun updateChildCount(childCount: Int) {
        _salesScreenState.value = _salesScreenState.value.copy(childCount = childCount)
        calculate()
    }

    private fun calculate() {
        val fare = calculator.calFare(
            adultNum = _salesScreenState.value.adultCount,
            childNum = _salesScreenState.value.childCount
        )
        val normalTicketCount = calculator.calNormalTicketCount(
            adultNum = _salesScreenState.value.adultCount,
            childNum = _salesScreenState.value.childCount
        )
        val accompanyTicketCount = calculator.calAccompanyTicketCount(
            adultNum = _salesScreenState.value.adultCount,
            childNum = _salesScreenState.value.childCount
        )
        _salesScreenState.value = _salesScreenState.value.copy(subFare = fare)
        _salesScreenState.value = _salesScreenState.value.copy(normalTicketCount = normalTicketCount)
        _salesScreenState.value = _salesScreenState.value.copy(accompanyTicketCount = accompanyTicketCount)
    }

}
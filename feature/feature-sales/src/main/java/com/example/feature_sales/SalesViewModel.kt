package com.example.feature_sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.GoodsRepository
import com.example.data.repository.RecordRepository
import com.example.model.Calculator
import com.example.model.CartItem
import com.example.model.Goods
import com.example.model.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SalesScreenState(
    val adultCount: Int = 0,
    val childCount: Int = 0,
    val selectedGoods: List<CartItem> = emptyList(),
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
    private val recordRepository: RecordRepository,
) : ViewModel() {
    sealed interface UiState {
        data object Initial : UiState
        data object Resetting : UiState
        data object ResetSuccess : UiState
        data object Idle : UiState
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
        val goodsSum = calculator.calGoodsSum(_salesScreenState.value.selectedGoods)

        _salesScreenState.value = _salesScreenState.value.copy(subFare = fare)
        _salesScreenState.value =
            _salesScreenState.value.copy(normalTicketCount = normalTicketCount)
        _salesScreenState.value =
            _salesScreenState.value.copy(accompanyTicketCount = accompanyTicketCount)
        _salesScreenState.value = _salesScreenState.value.copy(subGoods = goodsSum)
        _salesScreenState.value = _salesScreenState.value.copy(total = fare + goodsSum)
    }

    fun addItem(goods: Goods) {
        val existingItem = _salesScreenState.value.selectedGoods.find { it.goods.id == goods.id }
        if (existingItem != null) {
            val updatedGoodsList = _salesScreenState.value.selectedGoods.map { cartItem ->
                if (cartItem.goods.id == goods.id) {
                    cartItem.copy(quantity = cartItem.quantity + 1)
                } else {
                    cartItem
                }
            }
            _salesScreenState.value = _salesScreenState.value.copy(selectedGoods = updatedGoodsList)
            calculate()
        } else {
            _salesScreenState.value = _salesScreenState.value.copy(
                selectedGoods = _salesScreenState.value.selectedGoods + CartItem(goods, 1)
            )
            calculate()
        }
    }

    fun minusItem(cartItem: CartItem) {
        val existingItem =
            _salesScreenState.value.selectedGoods.find { it.goods.id == cartItem.goods.id }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                val updatedGoodsList = _salesScreenState.value.selectedGoods.map { currentItem ->
                    if (currentItem.goods.id == cartItem.goods.id) {
                        currentItem.copy(quantity = currentItem.quantity - 1)
                    } else {
                        currentItem
                    }
                }
                _salesScreenState.value =
                    _salesScreenState.value.copy(selectedGoods = updatedGoodsList)
                calculate()
            } else {
                _salesScreenState.value = _salesScreenState.value.copy(
                    selectedGoods = _salesScreenState.value.selectedGoods - cartItem
                )
                calculate()
            }
        }
    }

    fun plusItem(cartItem: CartItem) {
        val existingItem =
            _salesScreenState.value.selectedGoods.find { it.goods.id == cartItem.goods.id }
        if (existingItem != null) {
            val updatedGoodsList = _salesScreenState.value.selectedGoods.map { currentItem ->
                if (currentItem.goods.id == cartItem.goods.id) {
                    currentItem.copy(quantity = currentItem.quantity + 1)
                } else {
                    currentItem
                }
            }
            _salesScreenState.value = _salesScreenState.value.copy(selectedGoods = updatedGoodsList)
            calculate()
        }
    }

    fun deleteItem(cartItem: CartItem) {
        _salesScreenState.value = _salesScreenState.value.copy(
            selectedGoods = _salesScreenState.value.selectedGoods - cartItem
        )
        calculate()
    }

    fun saveRecord() {
        val record = Record(
            id = 0,
            time = System.currentTimeMillis(),
            total = _salesScreenState.value.total,
            fareSales = _salesScreenState.value.subFare,
            goodsSales = _salesScreenState.value.subGoods,
            adult = _salesScreenState.value.adultCount,
            child = _salesScreenState.value.childCount,
            goodsList = _salesScreenState.value.selectedGoods,
            memo = ""
        )
        viewModelScope.launch {
            try {
                recordRepository.add(record)
            } catch (e: Exception) {
                _uiState.value = UiState.Resetting
            }
        }
    }

}
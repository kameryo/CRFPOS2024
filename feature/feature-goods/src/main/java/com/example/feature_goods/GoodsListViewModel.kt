package com.example.feature_goods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.GoodsRepository
import com.example.model.Goods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoodsListViewModel @Inject constructor(
    private val repository: GoodsRepository,
) : ViewModel() {

    val items = repository.getAll()

    fun update(goods: Goods) {
        viewModelScope.launch {
            try {
                repository.update(goods)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

}
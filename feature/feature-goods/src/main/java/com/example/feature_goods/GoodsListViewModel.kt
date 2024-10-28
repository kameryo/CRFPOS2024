package com.example.feature_goods

import androidx.lifecycle.ViewModel
import com.example.data.repository.GoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoodsListViewModel @Inject constructor(
    private val repository: GoodsRepository,
) : ViewModel() {

    val items = repository.getAll()

//    fun update(goods: Goods) {
//        viewModelScope.launch {
//            try {
//                repository.update(goods)
//            } catch (e: Exception) {
//                println(e)
//            }
//        }
//    }

}
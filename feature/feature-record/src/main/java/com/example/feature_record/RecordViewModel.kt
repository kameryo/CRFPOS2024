package com.example.feature_record

import androidx.lifecycle.ViewModel
import com.example.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: RecordRepository,
) : ViewModel() {

    val items = repository.getAll()
}
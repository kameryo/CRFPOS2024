package com.example.feature_record

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummarizeRecordViewModel @Inject constructor(
    private val repository: RecordRepository,
) : ViewModel() {

    val items = repository.getDateList()

    fun exportRecordToCSV(date: String, context: Context?) {
        viewModelScope.launch {
            try {
                val recordList = repository.getDiaryData(date)
                val exporter = Exporter()
                exporter.writeRecordsToCSV(recordList, date, context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
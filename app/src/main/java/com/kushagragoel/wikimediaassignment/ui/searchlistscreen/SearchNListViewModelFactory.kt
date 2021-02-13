package com.kushagragoel.wikimediaassignment.ui.searchlistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kushagragoel.wikimediaassignment.database.SearchDatabaseDao

class SearchNListViewModelFactory(
    private val dataSource: SearchDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchNListViewModel::class.java)) {
            return SearchNListViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
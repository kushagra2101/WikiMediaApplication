package com.kushagragoel.wikimediaassignment.ui.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kushagragoel.wikimediaassignment.database.SearchDatabaseDao
import kotlinx.coroutines.*

class HomeScreenViewModel(dataSource: SearchDatabaseDao) : ViewModel() {

    val db = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Request a toast by setting this value to true.
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean?>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    val recentSearches = db.getAllPastSearches()

    fun onDeleteButtonClick() {
        uiScope.launch {
            // Clear the database table.
            clear()

            // Show a snackbar message, because it's friendly.
            _showSnackbarEvent.value = true
        }
    }

    val deleteButtonVisible = Transformations.map(recentSearches) {
        it?.isNullOrEmpty()?.not()
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            db.clearPastSearches()
        }
    }

    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }


}
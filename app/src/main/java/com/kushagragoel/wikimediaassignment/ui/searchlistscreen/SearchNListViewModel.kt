package com.kushagragoel.wikimediaassignment.ui.searchlistscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kushagragoel.wikimediaassignment.database.PastSearchEntity
import com.kushagragoel.wikimediaassignment.database.SearchDatabaseDao
import com.kushagragoel.wikimediaassignment.network.WikiMediaApi
import com.kushagragoel.wikimediaassignment.network.model.Page
import com.kushagragoel.wikimediaassignment.network.model.WikiMediaOutputBean
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchNListViewModel(dataSource: SearchDatabaseDao) : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<WikiMediaOutputBean>()

    // The external immutable LiveData for the response String
    val response: LiveData<WikiMediaOutputBean>
        get() = _response

    private val _is_api_in_progress = MutableLiveData<Boolean>()
    val isApiInProgress : LiveData<Boolean>
        get() = _is_api_in_progress

    private val _dataInsertionSuccess = MutableLiveData<Boolean>()
    val dataInsertionSuccess: LiveData<Boolean?>
        get() = _dataInsertionSuccess

    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    fun getWikiMediaApiData(searchQuery: String) {
        _is_api_in_progress.value = true
        WikiMediaApi.retrofitService.getSearchList(searchQuery)?.enqueue(object:
            Callback<WikiMediaOutputBean> {
            override fun onResponse(
                call: Call<WikiMediaOutputBean>,
                response: Response<WikiMediaOutputBean>
            ) {
                _response.value = response.body()
                _is_api_in_progress.value = false
            }

            override fun onFailure(call: Call<WikiMediaOutputBean>, t: Throwable) {
                _response.value = null
                _is_api_in_progress.value = false
            }

        })
    }

    fun storeRecentData(page: Page) {
        val searchEntity = PastSearchEntity(title = page.title?:"",
            imgUrl = page.thumbnail?.source?:"", webUrl = page.canonicalurl?:"")
        uiScope.launch {
            insert(searchEntity)
        }
        _dataInsertionSuccess.value = true
    }

    fun dataSuccessfullyAddedInDB() {
        _dataInsertionSuccess.value = null
    }

    private suspend fun insert(searchEntity: PastSearchEntity) {
        withContext(Dispatchers.IO) {
            database.insert(searchEntity)
        }
    }

}
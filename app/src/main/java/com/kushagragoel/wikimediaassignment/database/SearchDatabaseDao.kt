package com.kushagragoel.wikimediaassignment.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SearchDatabaseDao {
    @Insert
    fun insert(searchEntity: PastSearchEntity)

    @Update
    fun updateSearchEntry(searchEntity: PastSearchEntity)

    @Query("SELECT * from search_history ORDER BY date_added DESC")
    fun getAllPastSearches(): LiveData<List<PastSearchEntity>>

    @Query("SELECT * FROM search_history WHERE searchId = :key")
    fun getSearchEntity(key: Long): PastSearchEntity?

    @Query("DELETE from search_history")
    fun clearPastSearches()
}
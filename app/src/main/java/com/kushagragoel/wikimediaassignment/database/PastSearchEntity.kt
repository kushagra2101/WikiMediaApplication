package com.kushagragoel.wikimediaassignment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class PastSearchEntity(
    @PrimaryKey(autoGenerate = true) val searchId: Long = 0L,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "imgUrl") val imgUrl: String,
    @ColumnInfo(name = "webUrl") val webUrl: String,
    @ColumnInfo(name = "date_added") val date_added: Long = System.currentTimeMillis()
)
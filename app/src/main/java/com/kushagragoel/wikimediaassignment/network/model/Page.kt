package com.kushagragoel.wikimediaassignment.network.model

data class Page(
    val canonicalurl: String?,
    val contentmodel: String,
    val editurl: String,
    val fullurl: String,
    val index: Int,
    val lastrevid: Int,
    val length: Int,
    val ns: Int,
    val pageid: Int,
    val pagelanguage: String,
    val pagelanguagedir: String,
    val pagelanguagehtmlcode: String,
    val terms: Terms,
    val thumbnail: Thumbnail,
    val title: String?,
    val touched: String
)
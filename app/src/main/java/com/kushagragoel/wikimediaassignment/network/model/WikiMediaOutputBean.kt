package com.kushagragoel.wikimediaassignment.network.model

data class WikiMediaOutputBean(
    val batchcomplete: Boolean,
    val `continue`: Continue,
    val query: Query
)
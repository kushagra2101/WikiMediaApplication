package com.kushagragoel.wikimediaassignment.network.model

data class Query(
    val pages: List<Page>,
    val redirects: List<Redirect>
)
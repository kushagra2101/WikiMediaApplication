package com.kushagragoel.wikimediaassignment.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ModuleConstants {
    companion object {
        const val WIKI_URL_BUNDLE_KEY_NAME = "click_Item_Web_Url"
        const val HOME_PAGE = "home_page"
        const val LISTING_PAGE = "listing_page"

        fun checkNetworkConnectivity(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
    }
}
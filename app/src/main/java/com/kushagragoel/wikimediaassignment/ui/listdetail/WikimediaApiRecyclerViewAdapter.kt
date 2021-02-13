package com.kushagragoel.wikimediaassignment.ui.listdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kushagragoel.wikimediaassignment.R
import com.kushagragoel.wikimediaassignment.database.PastSearchEntity
import com.kushagragoel.wikimediaassignment.network.model.Page

class WikimediaApiRecyclerViewAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<WikimediaApiRecyclerViewAdapter.SearchItemViewHolder>() {

    private var searchItemList: MutableList<Page>? = null
    private var itemList: MutableList<PastSearchEntity>? = null
    private var flowType = -1

    constructor(searchItemList: MutableList<Page>?, clickListener: ClickListener) :
            this(clickListener) {
        this.searchItemList = searchItemList
        flowType = 1
    }

    constructor(clickListener: ClickListener, itemList: MutableList<PastSearchEntity>?) : this(
        clickListener
    ) {
        this.itemList = itemList
        flowType = 2
    }

    class SearchItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView = view.findViewById<TextView>(R.id.itemTextView)
        val searchItemImageView = view.findViewById<ImageView>(R.id.itemImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_list, parent, false)
        return SearchItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        if (flowType == 1) {
            val item: Page? = searchItemList?.get(position)
            holder.titleTextView.text = item?.title ?: ""
            item?.thumbnail?.source?.let {
                Glide.with(holder.searchItemImageView.context)
                    .load(it)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(holder.searchItemImageView)
            }
            holder.view.setOnClickListener {
                item?.let {
                    clickListener.onSearchItemClick(item)
                }
            }
        } else {
            val recentSearchEntity = itemList?.get(position)
            holder.titleTextView.text = recentSearchEntity?.title ?: ""
            if (!recentSearchEntity?.imgUrl.isNullOrEmpty()) {
                Glide.with(holder.searchItemImageView.context)
                    .load(recentSearchEntity?.imgUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(holder.searchItemImageView)
            }
            holder.view.setOnClickListener {
                recentSearchEntity?.let {
                    clickListener.onSearchItemClick(recentSearchEntity)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (flowType == 1)
            return searchItemList?.size?:0
        return itemList?.size?:0
    }

    interface ClickListener {
        fun onSearchItemClick(searchItemPage: Page)
        fun onSearchItemClick(searchItem: PastSearchEntity)
    }

}
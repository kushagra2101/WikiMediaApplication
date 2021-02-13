package com.kushagragoel.wikimediaassignment.ui.searchlistscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kushagragoel.wikimediaassignment.R
import com.kushagragoel.wikimediaassignment.database.PastSearchEntity
import com.kushagragoel.wikimediaassignment.database.SearchDatabase
import com.kushagragoel.wikimediaassignment.helper.ModuleConstants
import com.kushagragoel.wikimediaassignment.network.model.Page
import com.kushagragoel.wikimediaassignment.ui.listdetail.WikimediaApiRecyclerViewAdapter

class SearchNListFragment : Fragment() {

    companion object {
        fun newInstance() = SearchNListFragment()
    }

    private lateinit var viewModel: SearchNListViewModel

    private var searchList: MutableList<Page> = ArrayList()
    private lateinit var adapter: WikimediaApiRecyclerViewAdapter

    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    private var clickedItem: Page? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_n_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireNotNull(this.activity).application
        val db = SearchDatabase.getInstance(application).searchDatabaseDao
        val searchNListViewModelFactory = SearchNListViewModelFactory(db)
        viewModel = ViewModelProvider(this,searchNListViewModelFactory).get(SearchNListViewModel::class.java)

        viewModel.response.observe(viewLifecycleOwner, Observer {
            it.query?.pages?.let { page ->
                run {
                    searchList.clear()
                    searchList.addAll(page)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.isApiInProgress.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.dataInsertionSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                if (!clickedItem?.canonicalurl.isNullOrEmpty()) {
                    val bundle = Bundle()
                    bundle.putString("click_Item_Web_Url", clickedItem!!.canonicalurl)
                    this@SearchNListFragment.findNavController()
                        .navigate(R.id.action_searchNListFragment_to_searchDetailFragment, bundle)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.url_not_provided_by_api),Toast.LENGTH_LONG).show()
                }
                viewModel.dataSuccessfullyAddedInDB()
                clickedItem = null
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.searchView)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WikimediaApiRecyclerViewAdapter(
            searchList,
            object : WikimediaApiRecyclerViewAdapter.ClickListener {
                override fun onSearchItemClick(searchItemPage: Page) {
                    if (ModuleConstants.checkNetworkConnectivity(requireContext())) {
                        clickedItem = searchItemPage
                        viewModel.storeRecentData(searchItemPage)
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.no_network_connection),
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onSearchItemClick(searchItem: PastSearchEntity) {
                    TODO("Not yet implemented")
                }
            })
        recyclerView.adapter = adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    if (ModuleConstants.checkNetworkConnectivity(requireContext()))
                        viewModel.getWikiMediaApiData(query)
                    else {
                        Toast.makeText(requireContext(), getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

}
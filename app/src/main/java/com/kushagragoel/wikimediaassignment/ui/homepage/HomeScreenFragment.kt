package com.kushagragoel.wikimediaassignment.ui.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kushagragoel.wikimediaassignment.R
import com.kushagragoel.wikimediaassignment.database.PastSearchEntity
import com.kushagragoel.wikimediaassignment.database.SearchDatabase
import com.kushagragoel.wikimediaassignment.databinding.HomeScreenFragmentBinding
import com.kushagragoel.wikimediaassignment.helper.ModuleConstants
import com.kushagragoel.wikimediaassignment.network.model.Page
import com.kushagragoel.wikimediaassignment.ui.listdetail.WikimediaApiRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class HomeScreenFragment : Fragment() {

    companion object {
        fun newInstance() = HomeScreenFragment()
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the HomeScreenFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeScreenFragmentBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application

        val dataSource = SearchDatabase.getInstance(application).searchDatabaseDao
        val viewModelFactory = HomeScreenViewModelFactory(dataSource)

        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeScreenViewModel::class.java)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.homeScreenViewModel = viewModel

        binding.startSearchButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_homeScreenFragment_to_searchNListFragment)
        }

        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                // Reset state to make sure the toast is only shown once, even if the device
                // has a configuration change.
                viewModel.doneShowingSnackbar()
            }
        })

        binding.recentSearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        var itemList: MutableList<PastSearchEntity>? = ArrayList()
        val adapter =
            WikimediaApiRecyclerViewAdapter(object : WikimediaApiRecyclerViewAdapter.ClickListener {
                override fun onSearchItemClick(searchItemPage: Page) {
                    TODO("Not yet implemented")
                }

                override fun onSearchItemClick(searchItem: PastSearchEntity) {
                    val bundle = Bundle()
                    bundle.putString(ModuleConstants.WIKI_URL_BUNDLE_KEY_NAME, searchItem.webUrl)
                    this@HomeScreenFragment.findNavController()
                        .navigate(R.id.action_homeScreenFragment_to_searchDetailFragment, bundle)
                }
            }, itemList)
        binding.recentSearchRecyclerView.adapter = adapter

        viewModel.recentSearches.observe(requireActivity(), androidx.lifecycle.Observer {
            itemList?.clear()
            itemList?.addAll(it)
            adapter.notifyDataSetChanged()
            if(it.isNullOrEmpty()) {
                binding.noRecentSearchHistory.visibility = View.VISIBLE
                binding.recentSearchRecyclerView.visibility = View.GONE
            } else {
                binding.noRecentSearchHistory.visibility = View.GONE
                binding.recentSearchRecyclerView.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

}
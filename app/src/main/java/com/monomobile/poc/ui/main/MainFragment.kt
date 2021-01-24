package com.monomobile.poc.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.monomobile.poc.R
import com.monomobile.poc.adapter.ItemsAdapter
import com.monomobile.poc.api.Resource
import com.monomobile.poc.api.Status
import com.monomobile.poc.model.ArtistItem
import com.monomobile.poc.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    interface OnFragmentInteractionListener {
        fun toggleFilter()
        fun setIdlingResource(state: Boolean)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by sharedViewModel()
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        strItems.setOnRefreshListener {
            setItems()
            strItems.isRefreshing = false
        }

        tvFilter.isEnabled = false
        viewModel.artists.observe (viewLifecycleOwner, Observer{ result ->
            result ?: return@Observer

            when (result.status) {
                Status.SUCCESS -> {
                    result.data?.let {
                        setAdapter(it.sortedBy { item -> item.name }) // sort by name
                        Toast.makeText(context, "${it.size} items retrieved", Toast.LENGTH_SHORT)
                            .show()
                        tvFilter.isEnabled = true
                    }
                    spinner.visibility = View.GONE
                }
                Status.ERROR -> {
                    tvFilter.isEnabled = false
                    result.message?.let { error ->
                        Toast.makeText(
                            context,
                            "Error: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    spinner.visibility = View.GONE
                }
                Status.LOADING -> spinner.visibility = View.VISIBLE
            }

            tvFilter.setTextColor(getColor(this@MainFragment.requireContext(), R.color.colorBlue))
            listener?.setIdlingResource(true)
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                (rvItems.adapter as ItemsAdapter).getFilter().filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        tvFilter.setOnClickListener {
            listener?.toggleFilter()
        }

        getSearchResults()
    }

    private fun getSearchResults() {
        listener?.setIdlingResource(false)
        viewModel.getSearchResults()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun setItems() {
        getSearchResults()
        searchView.setQuery("", false)
    }

    private fun setAdapter(itemList : List<ArtistItem>) {
        rvItems.adapter = ItemsAdapter(itemList) {
            Intent(activity, DetailActivity::class.java).apply {
                putExtra("img", it?.img)
                putExtra("name", it?.name)
                putExtra("nickname", it?.nickname)
                putExtra("appearance", it?.appearance.toString())
                putExtra("occupation", it?.occupation.toString())
                putExtra("status", it?.status)
                startActivity(this)
            }
        }
    }

    fun setFilter(filter: String) {
        val adapter = rvItems.adapter as ItemsAdapter
        adapter.appearance = filter
        adapter.getFilter().filter(searchView.query)
    }
}

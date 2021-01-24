package com.monomobile.poc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.monomobile.poc.R.*
import com.monomobile.poc.adapter.SeasonsAdapter
import com.monomobile.poc.api.Status
import com.monomobile.poc.model.SeasonItem
import com.monomobile.poc.ui.main.MainFragment
import com.monomobile.poc.ui.main.MainViewModel
import kotlinx.android.synthetic.main.filter_drawer.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.test.espresso.IdlingResource

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener {

    private var mIdlingResource: SimpleIdlingResource? = null

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) {
            mIdlingResource = SimpleIdlingResource()
        }
        return mIdlingResource as SimpleIdlingResource
    }

    private val tagMainFragment = "mainFragment"
    private val viewModel: MainViewModel by viewModel()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(id.container, MainFragment.newInstance(), tagMainFragment)
                .commitNow()
        }

        toolbar.apply {
            setTitle(string.app_name)
            inflateMenu(R.menu.actions)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.refresh -> {
                        val mainFragment: MainFragment = supportFragmentManager.findFragmentByTag(
                            tagMainFragment
                        ) as MainFragment
                        mainFragment.setItems()
                        true
                    }
                    else -> false
                }
            }
        }

        viewModel.artists.observe(this, Observer { result ->
            result ?: return@Observer

            when (result.status) {
                Status.SUCCESS -> {
                    result.data?.let {
                        rvSeasons.apply {
                            layoutManager = LinearLayoutManager(context)
                            addItemDecoration(
                                DividerItemDecoration(
                                    context,
                                    DividerItemDecoration.VERTICAL
                                )
                            )
                            val seasonList = mutableSetOf<String>()
                            it.forEach { it.appearance?.let { seasons -> seasonList.addAll(seasons) } }
                            adapter = SeasonsAdapter(
                                seasonList.sorted().map { SeasonItem(title = it) })
                        }

                    }
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        })

        ibExit.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.END)
        }

        tvReset.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.END)
            val adapter = rvSeasons.adapter as SeasonsAdapter
            adapter.resetSelected()
            tvFilter.setTextColor(getColor(color.colorBlue))
            val mainFragment : MainFragment = supportFragmentManager.findFragmentByTag(
                tagMainFragment
            ) as MainFragment
            mainFragment.setFilter("")
        }

        buttonResults.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.END)
            val mainFragment : MainFragment = supportFragmentManager.findFragmentByTag(
                tagMainFragment
            ) as MainFragment
            val adapter = rvSeasons.adapter as SeasonsAdapter
            val selected = adapter.getSelected()
            if(selected.isEmpty()) {
                tvFilter.setTextColor(getColor(color.colorBlue))
            } else {
                tvFilter.setTextColor(getColor(color.colorRed))
            }
            mainFragment.setFilter(selected)
        }

    }

    override fun toggleFilter() {
        drawer_layout.openDrawer(GravityCompat.END)
    }

    override fun setIdlingResource(state: Boolean) {
        mIdlingResource?.setIdleState(state)
    }

}
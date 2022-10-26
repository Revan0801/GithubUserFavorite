package com.example.githubuserfavorite.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfavorite.R
import com.example.githubuserfavorite.data.model.SearchItem
import com.example.githubuserfavorite.databinding.ActivityMainBinding
import com.example.githubuserfavorite.setting.DataStoreViewModel
import com.example.githubuserfavorite.setting.DataStoreViewModelFactory
import com.example.githubuserfavorite.setting.SettingPreferences
import com.example.githubuserfavorite.ui.adapter.SearchAdapter
import com.example.githubuserfavorite.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private val searchViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        supportActionBar?.title = "Github User's Search"

        activityMainBinding.rvGitUser.setHasFixedSize(true)

        searchViewModel.userGitSearch.observe(this) { user ->
            setUserData(user)
        }

        searchViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        searchViewModel.isDataFound.observe(this) {
            onDataFound(it)
        }

        showRecyclerView()
        darkMode()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val goFavorite = Intent(this, FavoriteActivity::class.java)
                startActivity(goFavorite)
                true
            }
            R.id.setting -> {
                val switchTheme = Intent(this, DarkModeSettingActivity::class.java)
                startActivity(switchTheme)
                true
            }
            else -> true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menuSearch).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    private fun setUserData(items: List<SearchItem>) {
        val listUser = ArrayList<SearchItem>()
        for (data in items) {
            val user = SearchItem(data.username, data.id, data.avatarUrl)
            listUser.addAll(listOf(user))
        }
        val searchAdapter = SearchAdapter(listUser)
        activityMainBinding.rvGitUser.adapter = searchAdapter

        searchAdapter.setOnItemCallback(object : SearchAdapter.OnItemClickCallback {

            override fun onItemClicked(data: SearchItem){
                val intentMoveToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                intentMoveToDetail.putExtra(DetailUserActivity.DETAIL_USER, data.username)
                startActivity(intentMoveToDetail)
            }
        })
    }

    private fun showRecyclerView() {
        showLoading(false)
        val layoutManager = LinearLayoutManager(this)
        activityMainBinding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvGitUser.layoutManager = GridLayoutManager(applicationContext, 2)
            } else {
                rvGitUser.layoutManager = layoutManager
            }
            val itemDecoration = DividerItemDecoration(applicationContext, layoutManager.orientation)
            rvGitUser.addItemDecoration(itemDecoration)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        activityMainBinding.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                userSearch.icUserSearch.visibility = View.INVISIBLE
                userSearch.tvUserSearch.visibility = View.INVISIBLE
                icUserNotFound.visibility = View.INVISIBLE
                tvUserNotFound.visibility = View.INVISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun onDataFound(found: Boolean) {
        activityMainBinding.apply {
            if (found){
                userSearch.icUserSearch.visibility = View.INVISIBLE
                userSearch.tvUserSearch.visibility = View.INVISIBLE
                icUserNotFound.visibility = View.INVISIBLE
                tvUserNotFound.visibility = View.INVISIBLE
                rvGitUser.visibility = View.VISIBLE

            } else {
                userSearch.icUserSearch.visibility = View.INVISIBLE
                userSearch.tvUserSearch.visibility = View.INVISIBLE
                icUserNotFound.visibility = View.VISIBLE
                tvUserNotFound.visibility = View.VISIBLE
                rvGitUser.visibility = View.INVISIBLE

                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.messageErrorSearch),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun darkMode() {
        val pref = SettingPreferences.getInstance(dataStore)
        dataStoreViewModel = ViewModelProvider(this, DataStoreViewModelFactory(pref))[DataStoreViewModel::class.java]

        dataStoreViewModel.getThemeSetting().observe(this){ isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
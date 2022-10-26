package com.example.githubuserfavorite.ui.activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfavorite.R
import com.example.githubuserfavorite.databinding.ActivityFavoriteBinding
import com.example.githubuserfavorite.helper.ViewModelFactory
import com.example.githubuserfavorite.ui.adapter.FavoriteAdapter
import com.example.githubuserfavorite.ui.viewmodel.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        supportActionBar?.title = "Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteBinding.rvFavorite.setHasFixedSize(true)

        showFavorite()
    }

    private fun showFavorite() {
        favoriteAdapter = FavoriteAdapter()

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavoriteUser().observe(this@FavoriteActivity) { favoriteList ->
            favoriteAdapter.setListFavorite(favoriteList)
            if (favoriteList.isNullOrEmpty()) {
                favoriteBinding.icUserNotFound.visibility = View.VISIBLE
                favoriteBinding.tvUserNotFound.visibility = View.VISIBLE
            }
        }

        favoriteBinding.rvFavorite.adapter = favoriteAdapter

        showRecyclerView()

        favoriteAdapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClicked(loginUser: String) {
                showAlertDialog(loginUser)
            }
        })
    }

    private fun showAlertDialog(loginUser: String) {
        val dialogTitle = getString(R.string.title_delete)
        val dialogMessage = getString(R.string.description_delete)

        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                favoriteViewModel.removeFavorite(loginUser)
                Snackbar.make(
                    this@FavoriteActivity.findViewById(android.R.id.content),
                    getString(R.string.messageDelete),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        favoriteBinding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvFavorite.layoutManager = GridLayoutManager(applicationContext, 2)
            } else {
                rvFavorite.layoutManager = layoutManager
            }
            val itemDecoration = DividerItemDecoration(applicationContext, layoutManager.orientation)
            rvFavorite.addItemDecoration(itemDecoration)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val DETAIL_USER = "login"
    }
}
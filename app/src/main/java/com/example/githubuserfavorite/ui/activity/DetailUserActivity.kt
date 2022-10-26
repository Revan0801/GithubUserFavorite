package com.example.githubuserfavorite.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserfavorite.R
import com.example.githubuserfavorite.data.local.Favorite
import com.example.githubuserfavorite.data.model.UserDetail
import com.example.githubuserfavorite.databinding.ActivityDetailUserBinding
import com.example.githubuserfavorite.helper.ViewModelFactory
import com.example.githubuserfavorite.ui.adapter.SectionPagerAdapter
import com.example.githubuserfavorite.ui.viewmodel.DetailViewModel
import com.example.githubuserfavorite.ui.viewmodel.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailUserBinding: ActivityDetailUserBinding
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val detailViewModel by viewModels<DetailViewModel>()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailUserBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailUserBinding.root)

        supportActionBar?.title = "Detail User's"

        detailViewModel.userGitDetail.observe(this) { detailUser ->
            setUserDetail(detailUser)
            favorite.let {
                it.username = detailUser.username
                it.name = detailUser.name
                it.avatarUrl = detailUser.avatarUrl
            }
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        showSectionPager()

        getDetail()

        clickShare()

    }

    private fun getDetail(){
        val userDetail = intent.getStringExtra(DETAIL_USER).toString()

        sectionPagerAdapter.username = userDetail
        detailViewModel.getUserDetail(userDetail)

        // Favorite
        favoriteViewModel = obtainViewModel(this)
        favoriteViewModel.getAllLoginUser().observe(this@DetailUserActivity) { userList ->
            if (userList.contains(userDetail)) {
                isFavorite = true
                detailUserBinding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_favorite
                    )
                )
            } else {
                detailUserBinding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_favorite_border
                    )
                )
            }
        }

        detailUserBinding.fabFavorite.setOnClickListener { view ->
            if (!isFavorite) {
                detailUserBinding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_favorite
                    )
                )
                favoriteViewModel.addFavorite(favorite)
                Snackbar.make(view, getString(R.string.messageAdd), Snackbar.LENGTH_LONG).show()
                isFavorite = true
            } else {
                showAlertDialog(userDetail, view)
            }
        }
    }

    private fun setUserDetail(detailUser: UserDetail) {
        detailUserBinding.apply {
            Log.d(TAG, detailUser.toString())
            headerUser.tvItemUsername.text = StringBuilder("@").append(detailUser.username)
            headerUser.tvItemName.text = detailUser.name
            headerUser.tvFollower.text = detailUser.followers.toString()
            headerUser.tvFollowing.text = detailUser.following.toString()
            headerUser.tvCompany.text = detailUser.company
            headerUser.tvLocation.text = detailUser.location
            headerUser.tvRepository.text = detailUser.publicRepos.toString()

            Glide.with(this@DetailUserActivity)
                .load(detailUser.avatarUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(headerUser.imgPhotoUser)
        }
    }

    private fun showSectionPager(){
        detailUserBinding.apply {
            sectionPagerAdapter = SectionPagerAdapter(this@DetailUserActivity)
            val viewPager: ViewPager2 = viewPager
            viewPager.adapter = sectionPagerAdapter

            val tabs: TabLayout = tabs
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

            supportActionBar?.elevation = 0f
        }

    }

    private fun showAlertDialog(loginUser: String, view: View) {
        val dialogTitle = getString(R.string.title_delete)
        val dialogMessage = getString(R.string.alertDelete)

        val alertDialogBuilder = AlertDialog.Builder(this@DetailUserActivity)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                favoriteViewModel.removeFavorite(loginUser)
                isFavorite = false
                Snackbar.make(view, getString(R.string.messageDelete), Snackbar.LENGTH_LONG).show()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            detailUserBinding.progressBarDetail.visibility = View.VISIBLE
        } else {
            detailUserBinding.progressBarDetail.visibility = View.INVISIBLE
            detailUserBinding.headerUser.shareProfile.visibility = View.VISIBLE
            detailUserBinding.fabFavorite.visibility = View.VISIBLE
        }
    }

    private fun clickShare() {
        detailUserBinding.headerUser.shareProfile.setOnClickListener { v ->
            if (v == detailUserBinding.headerUser.shareProfile) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                val name = detailUserBinding.headerUser.tvItemName.text
                share.putExtra(Intent.EXTRA_TEXT, name)
                startActivity(Intent.createChooser(share, "Share With"))
            }
        }
    }

    companion object {
        val favorite = Favorite()

        private const val TAG = "DetailUserActivity"
        const val DETAIL_USER = "login"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.Followers,
            R.string.Following
        )
    }
}
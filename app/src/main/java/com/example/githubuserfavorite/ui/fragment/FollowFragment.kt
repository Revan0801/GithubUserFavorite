package com.example.githubuserfavorite.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfavorite.data.model.FollowItem
import com.example.githubuserfavorite.databinding.FragmentFollowBinding
import com.example.githubuserfavorite.ui.adapter.FollowAdapter
import com.example.githubuserfavorite.ui.viewmodel.FollowViewModel

class FollowFragment : Fragment() {

    private lateinit var followBinding: FragmentFollowBinding
    private lateinit var username: String
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        followBinding = FragmentFollowBinding.inflate(layoutInflater)
        return followBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followViewModel.userFollowers.observe(viewLifecycleOwner) {
            setFollow(it)
        }

        followViewModel.userFollowing.observe(viewLifecycleOwner) {
            setFollow(it)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        setTab()
    }

    private fun setTab() {
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        username = arguments?.getString(FOLLOW)!!

        when (index) {
            1 -> followViewModel.getFollowers(username)
            2 -> followViewModel.getFollowing(username)
        }
    }

    private fun setFollow(items: ArrayList<FollowItem>) {
        val listUser = ArrayList<FollowItem>()
        for (data in items) {
            val follow = FollowItem(data.avatarUrl, data.username, data.id)
            listUser.addAll(listOf(follow))
        }
        val followersAdapter = FollowAdapter(listUser)
        followBinding.apply {
            rvFollowUser.layoutManager = LinearLayoutManager(context)
            rvFollowUser.adapter = followersAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            followBinding.progressBarFollow.visibility = View.VISIBLE
        } else {
            followBinding.progressBarFollow.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val FOLLOW = "follow"
    }
}
package com.example.githubuserfavorite.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserfavorite.R
import com.example.githubuserfavorite.data.local.Favorite
import com.example.githubuserfavorite.databinding.ItemRowFavoriteBinding
import com.example.githubuserfavorite.helper.FavoriteDiffCallback
import com.example.githubuserfavorite.ui.activity.DetailUserActivity
import com.example.githubuserfavorite.ui.activity.FavoriteActivity
import java.util.ArrayList

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    private val listFavorite = ArrayList<Favorite>()
    private lateinit var onItemCLickListener: OnItemClickListener

    fun setListFavorite(listFavorite: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, onItemCLickListener)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = listFavorite.size

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemCLickListener = listener
    }

    interface OnItemClickListener{
        fun onItemClicked(loginUser: String)
    }

    class ListViewHolder(
        private val binding: ItemRowFavoriteBinding,
        private val listener: OnItemClickListener
        ): RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            with(binding) {
                tvItemUsername.text = favorite.username
                tvItemID.text = favorite.name

                Glide.with(itemView.context)
                    .load(favorite.avatarUrl)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .circleCrop()
                    .into(photoUser)

                binding.btnDelete.setOnClickListener {
                    listener.onItemClicked(favorite.username)
                }

                itemView.setOnClickListener {
                    val intentToDetail = Intent(itemView.context, DetailUserActivity::class.java)
                    intentToDetail.putExtra(FavoriteActivity.DETAIL_USER, favorite.username)
                    itemView.context.startActivity(intentToDetail)
                }
            }
        }
    }
}
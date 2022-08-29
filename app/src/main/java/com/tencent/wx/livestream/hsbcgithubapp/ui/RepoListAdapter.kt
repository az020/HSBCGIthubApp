package com.tencent.wx.livestream.hsbcgithubapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tencent.wx.livestream.hsbcgithubapp.R
import com.tencent.wx.livestream.hsbcgithubapp.databinding.RepoItemBinding
import com.tencent.wx.livestream.hsbcgithubapp.networking.RepoItem

/**
 * The recyclerView adapter for the repository list in search fragment
 */
class RepoListAdapter: RecyclerView.Adapter<RepoItemViewHolder>() {
    private var repoList = mutableListOf<RepoItem>()

    fun setList(list: List<RepoItem>) {
        repoList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoItemViewHolder {
       val binding = DataBindingUtil.inflate<RepoItemBinding>(
           LayoutInflater.from(parent.context), R.layout.repo_item, parent, false)
        return RepoItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return repoList.size
    }

    override fun onBindViewHolder(holder: RepoItemViewHolder, position: Int) {
        holder.binding.repoItem = repoList[position]
//        Log.i("HSBC RepoListAdapter", "binding item = ${getItem(position).fullName}")
//        holder.binding.repoItem = getItem(position)
    }

}

class RepoItemViewHolder(val binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root) {

}

//object RepoItemDiffCallback : DiffUtil.ItemCallback<RepoItem>() {
//    override fun areItemsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
//        return oldItem == newItem
//    }
//
//    override fun areContentsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
//        return oldItem.fullName == newItem.fullName
//    }
//}

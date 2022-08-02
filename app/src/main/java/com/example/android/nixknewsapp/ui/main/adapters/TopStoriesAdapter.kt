package com.example.android.nixknewsapp.ui.main.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.android.nixknewsapp.R
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.databinding.ItemsTopStoriesBinding
import com.example.android.nixknewsapp.utils.Extensions.formatTimeAgo

class TopStoriesAdapter(
    private val onMenuClicked: (View, Article) -> Unit,
): ListAdapter<Article, TopStoriesAdapter.ArticleViewHolder>(
    object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
){

    inner class ArticleViewHolder(private val binding: ItemsTopStoriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivDots.setOnClickListener { view ->
                onMenuClicked(view, getItem(bindingAdapterPosition))
            }
        }

        fun bind(article: Article, context: Context) {
            Glide.with(context)
                .load(article.image_url)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.not_found_404)
                .into(binding.ivArticle)
            binding.apply {
                tvInnerCategory.text = removeGeneralCategory(article.categories)
                tvTitleArticle.text = article.title
                tvTime.text = article.published_at?.formatTimeAgo() ?: ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemsTopStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle, holder.itemView.context)
    }

    private fun removeGeneralCategory(categories: List<String>?): String {
        val mutableCategories = categories?.toMutableList() ?: mutableListOf("general")
        if (mutableCategories.size != 1 && mutableCategories.contains("general")) {
            mutableCategories.remove("general")
        }
        return mutableCategories.first().toString()
    }
}
package ru.madmax.madnotes.presentation.list_category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.madmax.madnotes.databinding.ItemCategoryBinding
import ru.madmax.madnotes.domain.model.entity.Category

class ListCategoryAdapter(
    val onCategoryClickListener: (category: Category) -> Unit
) : ListAdapter<Category, ListCategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, onClickListener: (category: Category) -> Unit) {
            binding.apply {
                root.setCardBackgroundColor(category.color)
                itemCategoryTitle.text = category.title

                root.setOnClickListener {
                    onClickListener(category)
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.categoryId == newItem.categoryId

        override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem) { category ->
            onCategoryClickListener(category)
        }
    }
}
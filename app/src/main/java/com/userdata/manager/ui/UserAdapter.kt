package com.userdata.manager.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.userdata.manager.data.local.UserEntity
import com.userdata.manager.databinding.ItemUserBinding
import androidx.core.graphics.toColorInt

class UserAdapter(
    private val onClick: (UserEntity) -> Unit,
    private val onDelete: (UserEntity) -> Unit,
    private val onSelectionChanged: (Int) -> Unit

) : ListAdapter<UserEntity, UserAdapter.UserViewHolder>(DiffCallback()) {
    private val selectedItems = mutableSetOf<Int>()

    inner class UserViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {

        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val user = getItem(position)
        holder.binding.user = user
        holder.binding.executePendingBindings()

        val isSelected = selectedItems.contains(user.id)

        holder.itemView.setBackgroundColor(
            if (isSelected)
                "#D0E8FF".toColorInt()
            else
                Color.WHITE
        )

        holder.binding.edit.setOnClickListener {
            if (selectedItems.isEmpty()) {
                onClick(user)
            }
        }

        holder.itemView.setOnClickListener {
            if (selectedItems.isNotEmpty()) {
                toggleSelection(
                    user.id,
                    holder.bindingAdapterPosition
                )

            } else {
                onClick(user)
            }
        }

        holder.itemView.setOnLongClickListener {
            toggleSelection(
                user.id,
                holder.bindingAdapterPosition
            )
            true
        }

        holder.binding.delete.setOnClickListener {
            onDelete(user)
        }
    }

    private fun toggleSelection(
        id: Int,
        position: Int
    ) {
        if (selectedItems.contains(id)) {
            selectedItems.remove(id)
        } else {
            selectedItems.add(id)
        }

        notifyItemChanged(position)
        onSelectionChanged(selectedItems.size)
    }

    fun getSelectedIds(): List<Int> {
        return selectedItems.toList()
    }

    fun clearSelection() {
        val oldSelected = selectedItems.toList()
        selectedItems.clear()

        oldSelected.forEach { id ->
            val position = currentList.indexOfFirst {
                    it.id == id
                }
            if (position != -1) {
                notifyItemChanged(position)
            }
        }
        onSelectionChanged(0)
    }

    class DiffCallback :
        DiffUtil.ItemCallback<UserEntity>() {

        override fun areItemsTheSame(
            oldItem: UserEntity,
            newItem: UserEntity
        ): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserEntity,
            newItem: UserEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}
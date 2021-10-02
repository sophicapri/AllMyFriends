package com.example.allmyfriends.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.allmyfriends.databinding.ItemPersonBinding
import com.example.allmyfriends.model.User

class PersonListAdapter : ListAdapter<User, PersonListAdapter.PersonViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(
            ItemPersonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        Log.d("Adapter", "onCreateViewHolder: item count = $itemCount")
        holder.bindTo(person)
    }

    fun clearList() {
        currentList.clear()
    }

    inner class PersonViewHolder(var binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(user: User) {
            binding.personName.text = user.name.first
        }
    }


    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User) =
                oldItem == newItem
        }
    }
}
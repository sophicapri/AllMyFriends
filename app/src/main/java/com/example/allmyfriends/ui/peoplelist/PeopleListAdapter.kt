package com.example.allmyfriends.ui.peoplelist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.allmyfriends.databinding.ItemPersonBinding
import com.example.allmyfriends.model.Person

class PeopleListAdapter(private var onPersonClickListener: OnPersonClickListener) : PagingDataAdapter<Person, PeopleListAdapter.PersonViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        if (person != null) {
            holder.bindTo(person)
        }
    }

    inner class PersonViewHolder(var binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindTo(person: Person) {
            itemView.setOnClickListener {
                onPersonClickListener.onPersonClick(person = person)
            }
            Log.d("adapter", "bindTo: item name = ${person.name}")
            binding.personName.text =  person.name.first
        }
    }


    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Person>() {

            override fun areItemsTheSame(oldItem: Person, newItem: Person) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Person, newItem: Person) =
                oldItem == newItem
        }
    }

    interface OnPersonClickListener{
        fun onPersonClick(person: Person)
    }
}
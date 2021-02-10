package com.example.remotepaging.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.remotepaging.R
import com.example.remotepaging.databinding.UserItemBinding
import com.example.remotepaging.model.User
import javax.inject.Inject


class UserAdapter  @Inject constructor() :
    PagingDataAdapter<User, UserAdapter.UserItemViewHolder>(UserItemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserItemViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holderItem: UserItemViewHolder, position: Int) {
        getItem(position)?.let { holderItem.bind(it) }
    }

    inner class UserItemViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) = with(binding) {
            Log.d("UserItem", item.email)
            binding.txtUserFname.text = item.firstName
            binding.txtUserEmail.text = item.email
            if (URLUtil.isValidUrl(item.avatar)) {
                Glide.with(binding.root.context).load(item.avatar).into(binding.imgUserAvatar)
            } else if (!TextUtils.isEmpty(item.avatar)){
                val bitmap: Bitmap = BitmapFactory.decodeFile(item.avatar)
                binding.imgUserAvatar.setImageBitmap(bitmap)
            } else {
                binding.imgUserAvatar.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_launcher_foreground))
            }
        }
    }

    object UserItemComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) =
            oldItem == newItem
    }


}
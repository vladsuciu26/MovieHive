package com.example.movietestapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movietestapp.data.dto.ReplyData
import com.example.movietestapp.databinding.ReplyProfileItemBinding

class ProfileRepliesAdapter (
    private val replies: List<ReplyData>,
    private val onReplyClick: (ReplyData) -> Unit,
) : RecyclerView.Adapter<ProfileRepliesAdapter.ReplyViewHolder>() {

    inner class ReplyViewHolder(private val binding: ReplyProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reply: ReplyData) {
            binding.replyText.text = "You left a reply on the movie with the id: ${reply.movieId}"
            binding.root.setOnClickListener {
                onReplyClick(reply)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val binding =
            ReplyProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(replies[position])
    }

    override fun getItemCount() = replies.size
}
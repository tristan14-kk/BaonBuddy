package com.mobdev.baonbuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R

class AvatarAdapter(
    private val avatars: List<Int>,
    private val onAvatarSelected: (Int) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    private var selectedPosition = -1

    class AvatarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarCard: CardView = view.findViewById(R.id.avatarCard)
        val avatarImage: ImageView = view.findViewById(R.id.avatarImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_avatar, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatarResId = avatars[position]
        holder.avatarImage.setImageResource(avatarResId)

        // Highlight selected avatar
        if (position == selectedPosition) {
            holder.avatarCard.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.green_primary)
            )
        } else {
            holder.avatarCard.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.navy_blue)
            )
        }

        holder.avatarCard.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onAvatarSelected(avatarResId)
        }
    }

    override fun getItemCount() = avatars.size
}
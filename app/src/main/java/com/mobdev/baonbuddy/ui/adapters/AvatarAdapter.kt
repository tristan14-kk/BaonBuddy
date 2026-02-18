package com.mobdev.baonbuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R

class AvatarAdapter(
    private val avatars: List<Int>,
    private val onAvatarSelected: (Int) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    private var selectedPosition = -1

    class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImage: ImageView = itemView.findViewById(R.id.avatarImage)
        val avatarCard: CardView = itemView.findViewById(R.id.avatarCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_avatar, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatarResId = avatars[position]
        holder.avatarImage.setImageResource(avatarResId)


        if (position == selectedPosition) {
            holder.avatarCard.setCardBackgroundColor(holder.itemView.context.getColor(R.color.positive_green))
        } else {
            holder.avatarCard.setCardBackgroundColor(holder.itemView.context.getColor(R.color.navy_blue))
        }

        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onAvatarSelected(avatarResId)
        }
    }

    override fun getItemCount(): Int = avatars.size
}
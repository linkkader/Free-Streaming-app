package com.linkkader.exoplayer.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.linkkader.exoplayer.R
import com.linkkader.exoplayer.VodActivity
import com.linkkader.exoplayer.anime
import com.linkkader.exoplayer.episode
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class RecyclerViewEpisodeAdapter(var context : Context, var list : List<episode>) : RecyclerView.Adapter<RecyclerViewEpisodeAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.findViewById<TextView>(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return  ViewHolder(inflater.inflate(R.layout.item_episode,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = list[position].title
        holder.itemView.setOnClickListener {
            VodActivity.url = list[position].url
            val i =
                Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(Intent(context,VodActivity::class.java).addFlags(i))
        }
    }

}

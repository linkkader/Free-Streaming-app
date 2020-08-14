package com.linkkader.exoplayer.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.linkkader.exoplayer.AnimeInfo
import com.linkkader.exoplayer.Fragment.FragmentInfo
import com.linkkader.exoplayer.R
import com.linkkader.exoplayer.anime
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(var context : Context, var list : List<anime>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<RoundedImageView>(R.id.img)
        val text = itemView.findViewById<TextView>(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return  ViewHolder(inflater.inflate(R.layout.item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = list[position].title
        if (list[position].img!="") Picasso.get().load(list[position].img).into(holder.img)
        holder.itemView.setOnClickListener {
            FragmentInfo.url = list[position].url
            context.startActivity(Intent(context,AnimeInfo::class.java))
        }
    }

}

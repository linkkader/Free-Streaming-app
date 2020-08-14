package com.linkkader.exoplayer.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.linkkader.exoplayer.Adapters.RecyclerViewEpisodeAdapter
import com.linkkader.exoplayer.R
import com.linkkader.exoplayer.episode
import com.linkkader.exoplayer.info
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup

class FragmentInfo : Fragment() {
    companion object{
        var url = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backgroundImg = view.findViewById<ImageView>(R.id.backgroundImg)
        val img = view.findViewById<RoundedImageView>(R.id.img)
        val title = view.findViewById<TextView>(R.id.title)
        val description = view.findViewById<TextView>(R.id.description)
        val info = info()
        var episodes = listOf<episode>()

        var checkThread = false
        Thread{
            val document = Jsoup.connect(url).maxBodySize(0).timeout(0).get()
            info.img = document.select("div.summary_image").toString().substringAfter("src=\"").substringBefore("\"")
            info.title = document.select("div.post-title").text()
            info.description = document.select("div.summary__content").text()
            //now get all episodes
            val elements = document.select("li.wp-manga-chapter")
            for (element in elements){
                val episode = episode()
                episode.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                episode.title = element.text()
                episodes = episodes.plus(episode)
            }
            checkThread = true
        }.start()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable{
            override fun run() {
                //when thread finish
                if (checkThread){
                    if (info.img!="") {
                        Picasso.get().load(info.img).into(img)
                        Picasso.get().load(info.img).into(backgroundImg)
                    }
                    title.text = info.title
                    description.text = info.description
                    FragmentEpisode.recyclerView.adapter =  RecyclerViewEpisodeAdapter(activity!!.applicationContext,episodes)

                }else mainHandler.postDelayed(this,1000)
            }
        })









    }
}
package com.linkkader.exoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linkkader.exoplayer.Adapters.RecyclerViewAdapter
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        var list = listOf<anime>()

        var checkThread = false
        Thread{
            val document = Jsoup.connect("https://voiranime.to/anime-genre/action/").maxBodySize(0).timeout(0).get()
            val elements = document.select("div.page-item-detail")
            for (element in elements){
                val anime = anime()
                anime.url = element.toString().substringAfter("href=\"").substringBefore("\"")
                anime.img = element.toString().substringAfter("src=\"").substringBefore("\"")
                anime.title = element.toString().substringAfter("title=\"").substringBefore("\"")
                list = list.plus(anime)
            }
            checkThread = true
        }.start()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable{
            override fun run() {
             if (checkThread){
                 //when thread finish
                 recyclerView.adapter = RecyclerViewAdapter(this@MainActivity,list)
             }else mainHandler.postDelayed(this,1000)
            }
        })
        /*var checkThread = false
        Thread{

            checkThread = true
        }.start()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable{
            override fun run() {
             if (checkThread){

             }else mainHandler.postDelayed(this,1000)
            }
        })


         */
    }
}
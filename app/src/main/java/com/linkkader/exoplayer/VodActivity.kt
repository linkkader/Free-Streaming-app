package com.linkkader.exoplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.jsoup.Jsoup

class VodActivity : AppCompatActivity() {
    companion object{
        var url = ""
    }
    lateinit var playerView: PlayerView
    lateinit var player: SimpleExoPlayer
    lateinit var progressBar: ProgressBar
    lateinit var forward : AppCompatImageView
    lateinit var replay : AppCompatImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_vod)
        fullScreen()

        forward = findViewById(R.id.forward)
        replay = findViewById(R.id.replay)
        forward.setOnClickListener{
            player.seekTo(player.currentPosition + 10*1000)
        }
        replay.setOnClickListener{
            player.seekTo(player.currentPosition - 10*1000)
        }

        playerView = findViewById(R.id.playerView)
        progressBar = findViewById(R.id.progressBar)
        initPlayer()

        getMp4(url)
        //loadVod("https://storage.googleapis.com/intense-idea-285908/_ZQY12MVL895/22a_1596965003143460.mp4")
    }
    fun initPlayer(){
        val adaptiveTrackSelection = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
        player = ExoPlayerFactory.newSimpleInstance(this
                ,DefaultRenderersFactory(this),DefaultTrackSelector(adaptiveTrackSelection),DefaultLoadControl())
        playerView.player = player
        player.addListener(object : Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){
                    ExoPlayer.STATE_BUFFERING -> progressBar.visibility = View.VISIBLE
                    ExoPlayer.STATE_READY -> progressBar.visibility = View.GONE
                }
            }
        })
    }
    fun loadVod(url : String){
        val dataSourceFactory = DefaultDataSourceFactory(this,Util.getUserAgent(this,"Exo"),DefaultBandwidthMeter())
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))
        player.prepare(mediaSource)
        player.playWhenReady = true
    }
    fun fullScreen(){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }
    fun getMp4(url: String){
        var mp4url = ""
        var checkThread = false
        Thread{
            var doc = Jsoup.connect(url).timeout(0).maxBodySize(0).get()
            val redirect = doc.select("iframe").toString().substringAfter("src=\"").substringBefore("\"")
            doc  = Jsoup.connect(redirect).timeout(0).maxBodySize(0).get()
            //get mp4 url
            val elements=doc.select("script")
            for (element in elements){
                if (element.toString().contains("eval(")&&element.toString().contains("mp4")){
                    mp4url ="https://"+element
                        .toString().substringBeforeLast("'.")
                        .substringAfterLast("|")+".gounlimited.to/"+elements[elements.size-2].toString()
                        .substringBeforeLast("|")
                        .substringBeforeLast("|")
                        .substringAfterLast("|").replace(" ","") + "/v.mp4"
                }
            }
            checkThread = true
        }.start()
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable{
            override fun run() {
                if (checkThread){
                    //when thead finish
                    loadVod(mp4url)
                }else mainHandler.postDelayed(this,1000)
            }
        })
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }
}
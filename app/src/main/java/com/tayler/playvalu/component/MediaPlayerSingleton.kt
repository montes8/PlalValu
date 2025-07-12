package com.tayler.playvalu.component

import android.media.MediaPlayer
import kotlin.concurrent.thread

object MediaPlayerSingleton : MediaPlayer(){

    var mediaPlayerSingleton   :MediaPlayer?=null

    fun playStart(path : String){
        if(path.isNotEmpty()){
            playStop()
            mediaPlayerSingleton = MediaPlayer()
            mediaPlayerSingleton?.setDataSource(path)
            mediaPlayerSingleton?.prepare()
            mediaPlayerSingleton?.start()

        }
    }

    fun playStop(){
        mediaPlayerSingleton.apply {
            mediaPlayerSingleton?.stop()
        }
    }

    fun playPause(){
        mediaPlayerSingleton.apply {
            mediaPlayerSingleton?.pause()
        }
    }
    fun playMusic(){
        mediaPlayerSingleton.apply {
            mediaPlayerSingleton?.start()
        }
    }

    fun playStateMusic(value : Boolean){
            if (value){
                playPause()
            }else{
                playMusic()
            }
    }

    fun playDuration(): Int{
        return mediaPlayerSingleton?.duration?:0
    }

    fun playCurrentPosition(): Int{
        return mediaPlayerSingleton?.currentPosition?:0
    }

    fun playReset(){
         mediaPlayerSingleton?.reset()
    }

    fun playSetDuration(duration : Int){
        mediaPlayerSingleton?.seekTo(duration)
    }

}
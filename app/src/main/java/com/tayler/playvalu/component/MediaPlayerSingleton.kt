package com.tayler.playvalu.component

import android.media.MediaPlayer

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

}
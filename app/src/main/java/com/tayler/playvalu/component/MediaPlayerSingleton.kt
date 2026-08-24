package com.tayler.playvalu.component

import android.media.MediaPlayer
import com.tayler.playvalu.model.MusicModel

object MediaPlayerSingleton {

    var mediaPlayer: MediaPlayer? = null
    var listMusic: List<MusicModel> = arrayListOf()
    var positionMusic = 0
    var positionDurationMusic = 0
    var DurationTotalMusic = 0

    fun playStart(path: String) {
        if (path.isNotEmpty()) {
            playStop()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                start()
            }
        }
    }

    fun playStartUpdate(path: String, duration: Int) {
        if (path.isNotEmpty()) {
            playStop()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                seekTo(duration)
                start()
            }
        }
    }

    fun playStop() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    fun playPause() {
        mediaPlayer?.pause()
    }

    fun playMusic() {
        mediaPlayer?.start()
    }

    fun playStateMusic(isPlaying: Boolean) {
        if (isPlaying) {
            playPause()
        } else {
            playMusic()
        }
    }

    fun playDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun playCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }
}
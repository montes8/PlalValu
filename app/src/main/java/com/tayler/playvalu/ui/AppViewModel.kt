package com.tayler.playvalu.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tayler.playvalu.component.MediaPlayerSingleton
import com.tayler.playvalu.model.MusicModel
import com.tayler.playvalu.ui.home.MusicUiState
import com.tayler.playvalu.ui.splash.InitUiEvent
import com.tayler.playvalu.utils.getFileMusic
import com.tayler.playvalu.utils.getFileMusicDeprecated
import com.tayler.playvalu.utils.validateApiAndroidR
import androidx.lifecycle.viewModelScope
import com.tayler.playvalu.utils.formatTimePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class AppViewModel @Inject constructor(
):BaseViewModel() {


    private val _eventFlow = MutableSharedFlow<InitUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    var uiStateDataMusic by mutableStateOf(MusicUiState())
    var uiStateMusic by mutableStateOf(MusicModel())
    var uiStatePosition by mutableIntStateOf(0)
    var musicDuration by mutableIntStateOf(0)
    var sliderPosition by mutableFloatStateOf(0f)
    var stateMusic by  mutableStateOf(false)
    var textProgress by  mutableStateOf("00:00")
    var visibleMusic by  mutableStateOf(false)

    var visibleToolbar by  mutableStateOf(false)
    var visibleMusicEmpty by  mutableStateOf(false)

    private var progressJob: Job? = null

    fun startProgressTimer() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                if (visibleMusic && stateMusic) {
                    val currentPos = MediaPlayerSingleton.playCurrentPosition()
                    sliderPosition = currentPos.toFloat()
                    textProgress = formatTimePlayer(currentPos)
                    
                    if (sliderPosition > musicDuration - 1000 && musicDuration > 0) {
                        nextMusic()
                    }
                }
                delay(500.milliseconds)
            }
        }
    }

    fun playMusic(index: Int) {
        visibleMusic = true
        stateMusic = true
        uiStatePosition = index
        uiStateMusic = uiStateDataMusic.listMusic[index]
        MediaPlayerSingleton.playStart(uiStateMusic.path)
        musicDuration = MediaPlayerSingleton.playDuration()
        startProgressTimer()
    }

    fun togglePlayPause() {
        MediaPlayerSingleton.playStateMusic(stateMusic)
        stateMusic = !stateMusic
    }

    fun nextMusic() {
        if (uiStatePosition < uiStateDataMusic.listMusic.size - 1) {
            playMusic(uiStatePosition + 1)
        }
    }

    fun previousMusic() {
        if (uiStatePosition > 0) {
            playMusic(uiStatePosition - 1)
        }
    }

    fun stopMusic() {
        MediaPlayerSingleton.playStop()
        visibleMusic = false
        stateMusic = false
        musicDuration = 0
        sliderPosition = 0f
        progressJob?.cancel()
    }

    fun onSeek(position: Float) {
        sliderPosition = position
        textProgress = formatTimePlayer(position.toInt())
        MediaPlayerSingleton.seekTo(position.toInt())
    }

    fun loadValidateLogin(){
        execute {
            delay(2000.milliseconds)
            _eventFlow.emit(InitUiEvent.NavigateToNext)
        }
    }

    fun loadMusic(context: Context){
        execute {
            val listFilter = if(validateApiAndroidR())context.getFileMusic() else{
                getFileMusicDeprecated()
            }
            MediaPlayerSingleton.listMusic = listFilter

            uiStateDataMusic = uiStateDataMusic.copy(listMusic = listFilter,uiStateLoading = false)
            visibleMusicEmpty = listFilter.isEmpty()
        }
    }
}



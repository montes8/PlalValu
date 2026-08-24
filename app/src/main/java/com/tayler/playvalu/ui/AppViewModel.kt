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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    var stateMusic by  mutableStateOf(true)
    var textProgress by  mutableStateOf("00:00")
    var visibleMusic by  mutableStateOf(false)

    var visibleToolbar by  mutableStateOf(false)
    var visibleMusicEmpty by  mutableStateOf(false)

    fun loadValidateLogin(){
        execute {
            delay(4000.milliseconds)
            _eventFlow.emit(InitUiEvent.NavigateToNext())
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



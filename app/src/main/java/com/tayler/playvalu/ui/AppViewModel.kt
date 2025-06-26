package com.tayler.playvalu.ui

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tayler.playvalu.model.MusicModel
import com.tayler.playvalu.ui.home.MusicUiState
import com.tayler.playvalu.ui.splash.InitUiEvent
import com.tayler.playvalu.usecases.AppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appUseCase: AppUseCase
):BaseViewModel() {


    private val _eventFlow = MutableSharedFlow<InitUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var uiStateDataMusic by mutableStateOf(MusicUiState())

    var uiStateMusic by mutableStateOf(MusicModel())
    var uiStatePosition by mutableIntStateOf(0)


    var uiStateLoading by mutableStateOf(true)

    fun loadValidateLogin(){
        execute {
            val response = appUseCase.getToken()
            delay(3000)
            _eventFlow.emit(InitUiEvent.NavigateToNext(response))
        }
    }

    fun loadMusic(){
        val listFilter : ArrayList<MusicModel> = ArrayList()
        execute {
            val songs = getMusic(Environment.getExternalStorageDirectory())
            for (item in songs){
                listFilter.add(MusicModel(name = item.name,path = item.path))
            }
            uiStateDataMusic = uiStateDataMusic.copy(listMusic = listFilter)
        }
    }
}



fun getMusic(root: File): ArrayList<File> {
    val filesMusic: ArrayList<File> = ArrayList()
    val files = root.listFiles()
    files?.let {
        for (item in it) {
            if (item.isDirectory && !item.isHidden) {
                filesMusic.addAll(getMusic(item))
            } else {
                if (item.name.endsWith(".mp3")) {
                    filesMusic.add(item)
                }
            }
        }
    }
    return filesMusic
}


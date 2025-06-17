package com.tayler.playvalu.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tayler.playvalu.model.MusicModel
import com.tayler.playvalu.ui.splash.InitUiEvent
import com.tayler.playvalu.usecases.AppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appUseCase: AppUseCase
):BaseViewModel() {


    private val _eventFlow = MutableSharedFlow<InitUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var uiStateListMusic by mutableStateOf(listOf<MusicModel>())

    init {
        execute {
            delay(500)
            loadMusic()
        }
    }

    fun loadValidateLogin(){
        execute {
            val response = appUseCase.getToken()
            delay(3000)
            _eventFlow.emit(InitUiEvent.NavigateToNext(response))
        }
    }

    fun loadMusic(){
        Log.d("listamuca","loadlist")
        execute {
            uiStateListMusic = arrayListOf(MusicModel(0,"La beriso","qwewqwew",true))
        }
    }
}
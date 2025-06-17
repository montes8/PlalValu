package com.tayler.playvalu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayler.playvalu.repository.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel( @IoDispatcher
                     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): ViewModel() {


    fun execute(loading: Boolean = true,func:suspend ()->Unit){
        viewModelScope.launch(ioDispatcher){
            try {
                func()
            }catch (ex:Exception){
            }
        }
    }
}
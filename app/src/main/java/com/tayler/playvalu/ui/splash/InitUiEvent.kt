package com.tayler.playvalu.ui.splash

sealed class InitUiEvent {
    class NavigateToNext(value : Boolean) : InitUiEvent()
}
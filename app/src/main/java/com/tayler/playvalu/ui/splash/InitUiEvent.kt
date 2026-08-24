package com.tayler.playvalu.ui.splash

sealed class InitUiEvent {
    data object NavigateToNext : InitUiEvent()
}
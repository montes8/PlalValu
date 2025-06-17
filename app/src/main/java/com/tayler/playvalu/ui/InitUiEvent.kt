package com.tayler.playvalu.ui

sealed class InitUiEvent {
    class NavigateToNext(value : Boolean) : InitUiEvent()
}
package com.tayler.playvalu.ui.home

import com.tayler.playvalu.model.MusicModel


data class MusicUiState(
    var listMusic: List<MusicModel> = emptyList(),
    var uiStateLoading : Boolean = true
)

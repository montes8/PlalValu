package com.tayler.playvalu.ui.search

import com.tayler.playvalu.model.JamendoTrack

data class SearchUiState(
    val listSearch: List<JamendoTrack> = emptyList(),
    val uiStateLoading: Boolean = false,
    val uiStateError: String? = null
)

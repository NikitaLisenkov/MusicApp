package com.example.someapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.someapp.domain.usecase.GetChartTracksUseCase
import com.example.someapp.domain.usecase.SearchTracksUseCase

class TracksViewModelFactory(
    private val getChartTracksUseCase: GetChartTracksUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TracksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TracksViewModel(getChartTracksUseCase, searchTracksUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
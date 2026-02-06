package com.example.moviesandmore.ui.theme

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle // Hilt injects this automatically
) : ViewModel() {

}

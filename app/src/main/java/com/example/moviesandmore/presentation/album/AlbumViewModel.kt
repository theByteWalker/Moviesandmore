package com.example.moviesandmore.presentation.album

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class AlbumViewState(
    val tempFileUrl: Uri? = null,
    val selectedPictures: List<ImageBitmap> = emptyList()
)

sealed class AlbumIntent {
    data object RequestCamera : AlbumIntent()
    data object OnPhotoCaptured : AlbumIntent()
    data class OnImagesPicked(val uris: List<Uri>) : AlbumIntent()
    data object ResetTempUri : AlbumIntent()
}

@HiltViewModel
class AlbumViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _viewState = MutableStateFlow(AlbumViewState())
    val viewState: StateFlow<AlbumViewState> = _viewState.asStateFlow()

    fun onIntent(intent: AlbumIntent) {
        when (intent) {
            is AlbumIntent.RequestCamera -> requestCamera()
            is AlbumIntent.OnPhotoCaptured -> onPhotoCaptured()
            is AlbumIntent.OnImagesPicked -> onImagesPicked(intent.uris)
            is AlbumIntent.ResetTempUri -> _viewState.update { it.copy(tempFileUrl = null) }
        }
    }

    private fun requestCamera() {
        val tempFile = File.createTempFile("camera_image_", ".jpg", context.cacheDir)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
        _viewState.update { it.copy(tempFileUrl = uri) }
    }

    private fun onPhotoCaptured() {
        val uri = _viewState.value.tempFileUrl ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = uriToBitmap(uri)
            if (bitmap != null) {
                _viewState.update {
                    it.copy(
                        selectedPictures = it.selectedPictures + bitmap,
                        tempFileUrl = null
                    )
                }
            }
        }
    }

    private fun onImagesPicked(uris: List<Uri>) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmaps = uris.mapNotNull { uriToBitmap(it) }
            _viewState.update {
                it.copy(selectedPictures = it.selectedPictures + bitmaps)
            }
        }
    }

    private fun uriToBitmap(uri: Uri): ImageBitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: Exception) {
            null
        }
    }
}

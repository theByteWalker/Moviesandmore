package com.example.moviesandmore.presentation.ui.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviesandmore.domain.Intent
import com.example.moviesandmore.presentation.AlbumViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AlbumScreen(innerPaddingValues: PaddingValues, viewModel: AlbumViewModel) {
    val viewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current

    // launches photo picker
    val pickImageFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { urls ->
            viewModel.onReceive(Intent.OnFinishPickingImagesWith(currentContext, urls))
        }

    // launches camera
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
            if (isImageSaved) {
                viewModel.onReceive(Intent.OnImageSavedWith(currentContext))
            } else {
                // handle image saving error or cancellation
                viewModel.onReceive(Intent.OnImageSavingCanceled)
            }
        }

    // launches camera permissions
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                viewModel.onReceive(Intent.OnPermissionGrantedWith(currentContext))
            } else {
                // handle permission denied such as:
                viewModel.onReceive(Intent.OnPermissionDenied)
            }
        }

    // this ensures that the camera is launched only once when the url of the temp file changes
    LaunchedEffect(key1 = viewState.tempFileUrl) {
        viewState.tempFileUrl?.let {
            cameraLauncher.launch(it)
        }
    }

    Column(modifier = Modifier.padding(innerPaddingValues).fillMaxSize()) {
        Row {
            Button(onClick = {
                // get user's permission first to use camera
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Text(text = "Take a photo")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                // Image picker does not require special permissions and can be activated right away
                val mediaRequest =
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                pickImageFromAlbumLauncher.launch(mediaRequest)
            }) {
                Text(text = "Pick a picture")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Selected Pictures")
        LazyVerticalGrid(modifier = Modifier.fillMaxWidth().heightIn(0.dp, 1200.dp),
            columns = Adaptive(150.dp),
            userScrollEnabled = false) {
            itemsIndexed(viewState.selectedPictures) { index, picture ->
                Image(modifier = Modifier.padding(8.dp),
                    bitmap = picture,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}
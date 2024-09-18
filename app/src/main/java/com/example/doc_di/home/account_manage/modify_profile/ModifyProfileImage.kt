package com.example.doc_di.home.account_manage.modify_profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ModifyProfileImage(
    imageUri: MutableState<Uri?>,
    imageBitmap: MutableState<ImageBitmap?>,
    bitmap: MutableState<Bitmap?>,
    context: Context,
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri.value = it
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val decodedSource = ImageDecoder.decodeBitmap(source)
            bitmap.value = decodedSource
            imageBitmap.value = decodedSource.asImageBitmap()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { tempBitmap: Bitmap? ->
        tempBitmap?.let {
            imageBitmap.value = it.asImageBitmap()
            bitmap.value = tempBitmap
        }
    }

    var showImagePickerDialog by rememberSaveable { mutableStateOf(false) }

    Image(
        bitmap = bitmap.value!!.asImageBitmap(),
        contentDescription = "프로필 기본 사진",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(96.dp)
            .clickable {
                showImagePickerDialog = true
            }
            .clip(RoundedCornerShape(24.dp))
    )

    if (showImagePickerDialog) {
        ImagePickerDialog(
            onDismiss = { showImagePickerDialog = false },
            onGalleryClick = {
                showImagePickerDialog = false
                galleryLauncher.launch("image/*")
            },
            onCameraClick = {
                showImagePickerDialog = false
                cameraLauncher.launch()
            }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
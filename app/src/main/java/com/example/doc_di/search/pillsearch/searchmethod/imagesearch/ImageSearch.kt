package com.example.doc_di.search.pillsearch.searchmethod.imagesearch

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.home.account_manage.modify_profile.ImagePickerDialog
import com.example.doc_di.search.SearchViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImageSearch(navController: NavController, searchViewModel: SearchViewModel) {
    val context = LocalContext.current
    val mainSearchColor = Color(0xFF1892FA)

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val decodedSource = ImageDecoder.decodeBitmap(source)
            bitmap = decodedSource
            imageBitmap = decodedSource.asImageBitmap()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { tempBitmap: Bitmap? ->
        tempBitmap?.let {
            imageBitmap = it.asImageBitmap()
            bitmap = tempBitmap
        }
    }

    var showImagePickerDialog by remember { mutableStateOf(false) }

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "정확한 인식을 위해 책상 위에 알약을 두고",
            color = Color.Black,
        )
        Text(
            text = "알약이 중앙에 오도록 촬영해주세요",
            color = Color.Black,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(25.dp))
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = "약 검색 사진",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                OutlinedCard(
                    onClick = { showImagePickerDialog = true },
                    shape = RoundedCornerShape(25.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, Color(0xFF007AEB)),
                    modifier = Modifier.fillMaxSize()
                ) { }
            }
        }
        Spacer(modifier = Modifier.weight(2f))
        Button(
            onClick = { showImagePickerDialog = true },
            colors = ButtonDefaults.textButtonColors(mainSearchColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "이미지 가져오기",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        if (imageBitmap != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (isNetworkAvailable(context)) {
                        searchViewModel.searchPillsByImage(context, bitmap!!)
                        navController.navigate(Routes.searchResult.route)
                    }
                    else {
                        Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.textButtonColors(mainSearchColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "검색",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
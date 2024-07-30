@file:Suppress("UnusedImport")

package com.example.doc_di.search.searchmethod

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.home.ImagePickerDialog
import com.example.doc_di.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchMethod(
    navController:NavController,
    searchViewModel: SearchViewModel,
    btmBarViewModel: BtmBarViewModel
) {
    val context = LocalContext.current

    val howSearchButtonColor = Color(0xFF007AEB)
    val mainSearchColor = Color(0xFF1892FA)

    var preIdentifier by remember { mutableStateOf("ex) TYER") }
    var sufIdentifier by remember { mutableStateOf("ex) 325") }
    val focusRequester = remember { FocusRequester() }

    val unselectedButtonColor = Color(0xFFF2F6F7)

    var prefixTextColor = Color(0xFFC4CACF)
    var suffixTextColor = Color(0xFFC4CACF)

    var selectedShape = remember { mutableStateOf("타원형")}
    var selectedColor = remember { mutableStateOf("하양")}


    var imageUri by remember { mutableStateOf<Uri?>(null)}
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null)}

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ uri : Uri? ->
        uri?.let {
            imageUri = it
            val source = if(Build.VERSION.SDK_INT < 28){
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            else{
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            imageBitmap = source.asImageBitmap()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ){bitmap: Bitmap? ->
        bitmap?.let {
            imageBitmap = it.asImageBitmap()
        }
    }

    var showImagePickerDialog by remember{ mutableStateOf(false)}

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 106.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .padding(start = 40.dp)
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickable { navController.popBackStack() }
            )

            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                for (i in 0 until searchViewModel.showSearch.size) {
                    Button(
                        onClick = {
                            for (j in 0 until searchViewModel.showSearch.size) {
                                searchViewModel.showSearch[j] = i == j
                            }
                        },
                        border = if (!searchViewModel.showSearch[i]) BorderStroke(
                            1.dp,
                            Color.LightGray
                        )
                        else null,
                        colors = if (searchViewModel.showSearch[i]) ButtonDefaults.textButtonColors(
                            howSearchButtonColor
                        )
                        else ButtonDefaults.textButtonColors(Color.Transparent),
                        modifier = Modifier.size(width = 124.dp, height = 48.dp)
                    ) {
                        Text(
                            text = searchViewModel.searchTitle[i],
                            color = if (searchViewModel.showSearch[i]) Color.White else Color.LightGray,
                            fontSize = 14.sp,
                            fontWeight = if (searchViewModel.showSearch[i]) FontWeight.Bold else null
                        )
                    }
                }
            }

            if(searchViewModel.showSearch[0])
                TextSearch(navController, searchViewModel)
            else if(searchViewModel.showSearch[1]){
                val options = mutableMapOf<String,String>()
                val textColor = Color(0xFF191D30)
                val shapes = listOf<String>("타원형", "원형", "장방형", "기타")
                val colors = listOf<String>("하양", "노랑", "분홍", "초록", "파랑", "주황", "연두", "빨강", "기타")
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 50.dp)
                ) {
                    androidx.compose.material.OutlinedTextField(
                        value = preIdentifier,
                        onValueChange = { preIdentifier = it },
                        label = {
                            Text(
                                text = "식별표시 앞",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF191D30),
                            )
                        },
                        textStyle = TextStyle(
                            color = prefixTextColor
                        ),
                        colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                          focusedBorderColor = Color(0xFF007AEB)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    preIdentifier = ""
                                    prefixTextColor = Color.Black
                                }
                            }
                    )

                    androidx.compose.material.OutlinedTextField(
                        value = sufIdentifier,
                        onValueChange = { sufIdentifier = it },
                        label = {
                            Text(
                                text = "식별표시 뒤",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF191D30),
                            )
                        },
                        textStyle = TextStyle(
                            color = suffixTextColor
                        ),
                        colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF007AEB)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    sufIdentifier = ""
                                    suffixTextColor = Color.Black
                                }
                            }
                    )

                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "모양",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                        )
                        Column(
                            modifier = Modifier
                                .width(196.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(unselectedButtonColor)
                        ) {
                            shapes.chunked(2).forEach { row ->
                                Row {
                                    row.forEach { shape ->
                                        ShapeColorBtn(
                                            shapeOrColor = shape,
                                            selectedShapeOrColor = selectedShape,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "색상",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Column(
                            modifier = Modifier
                                .width(254.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(unselectedButtonColor)
                        ) {
                            colors.chunked(3).forEach { row ->
                                Row {
                                    row.forEach { color ->
                                        ShapeColorBtn(
                                            shapeOrColor = color,
                                            selectedShapeOrColor = selectedColor,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    androidx.compose.material.Button(
                        onClick = {
                            options["text1"] = preIdentifier
                            options["text2"] = sufIdentifier
                            options["shape"] = selectedShape.value
                            options["color"] = selectedColor.value
                            searchViewModel.searchPillsByOptions(options)
                            navController.navigate(Routes.searchResult.route)
                        },
                        colors = androidx.compose.material.ButtonDefaults.textButtonColors(
                            mainSearchColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
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
            }

            if(searchViewModel.showSearch[2]){
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(286.dp)
                        .clip(RoundedCornerShape(25.dp))
                ){
                    if (imageBitmap != null){
                        Image(
                            bitmap = imageBitmap!!,
                            contentDescription = "약 검색 사진",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    else{
                        Image(
                            painter = painterResource(id = R.drawable.image_box),
                            contentDescription = "약 검색 사진 디폴트",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                androidx.compose.material.Button(
                    onClick = {
                        showImagePickerDialog = true
                    },
                    colors = androidx.compose.material.ButtonDefaults.textButtonColors(mainSearchColor),
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

                if(imageBitmap != null){

                    Spacer(modifier = Modifier.height(24.dp))

                    androidx.compose.material.Button(
                        onClick = {
                            navController.navigate(Routes.searchResult.route)
                        },
                        colors = androidx.compose.material.ButtonDefaults.textButtonColors(mainSearchColor),
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

                if (showImagePickerDialog){
                    ImagePickerDialog(
                        onDismiss = { showImagePickerDialog = false },
                        onGalleryClick = {
                            showImagePickerDialog=false
                            galleryLauncher.launch("image/*")
                        },
                        onCameraClick = {
                            showImagePickerDialog = false
                            cameraLauncher.launch()
                        }
                    )
                }
            }
        }
    }
}
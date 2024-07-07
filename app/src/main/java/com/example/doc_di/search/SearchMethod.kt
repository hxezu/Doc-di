@file:Suppress("UnusedImport")

package com.example.doc_di.search

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.Routes
import com.example.doc_di.home.ImagePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchMethod(
    navController:NavController,
    searchViewModel: SearchViewModel
) {
    val context = LocalContext.current

    var nameSearch by remember{ mutableStateOf("")}

    val howSearchButtonColor = Color(0xFF007AEB)
    val mainSearchColor = Color(0xFF1892FA)

    var preIdentifier by remember { mutableStateOf("TYER") }
    var sufIdentifier by remember { mutableStateOf("325") }
    val focusRequester = remember { FocusRequester() }

    val selectedButtonColor = Color.White
    val unselectedButtonColor = Color(0xFFF2F6F7)

    val selectedButtonTextColor = Color.Black
    val unselectedButtonTextColor = Color(0xFF616161)

    var selectedShape by remember { mutableStateOf("타원")}
    var selectedColor by remember { mutableStateOf("하양")}


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

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backarrow),
                    contentDescription = "이전",
                    modifier = Modifier.size(44.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "검색 닫기",
                    modifier = Modifier
                        .size(44.dp)
                        .clickable { navController.navigate(Routes.search.route) }
                )
            }

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

            if(searchViewModel.showSearch[0]){
                OutlinedTextField(
                    value = nameSearch,
                    onValueChange = {nameSearch = it},
                    leadingIcon = {
                        Row(
                            modifier = Modifier.padding(start = 12.dp) // 왼쪽에 여백 추가
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "제품명 검색",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "제품명 입력",
                            fontSize = 16.sp,
                            color = Color.LightGray,
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = howSearchButtonColor,
                        unfocusedBorderColor = howSearchButtonColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(vertical = 70.dp)
                        .size(336.dp, 57.dp)
                )

                androidx.compose.material.Button(
                    onClick = { navController.navigate(Routes.searchResult.route) },
                    colors = androidx.compose.material.ButtonDefaults.textButtonColors(mainSearchColor),
                    modifier = Modifier
                        .size(328.dp, 60.dp)
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

            if(searchViewModel.showSearch[1]){
                val textColor = Color(0xFF191D30)
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 114.dp)
                        .clickable { 
                            TODO("Spacer, fillmaxwidth contentpadding 사용해서 버튼 조정")
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp)
                    ) {
                        Text(
                            text = "식별표시 앞",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BasicTextField(
                            value = preIdentifier,
                            onValueChange = { preIdentifier = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.LightGray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        preIdentifier = ""
                                    }
                                }
                                .focusRequester(focusRequester)
                                .width(IntrinsicSize.Min)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp)
                    ) {
                        Text(
                            text = "식별표시 뒤",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BasicTextField(
                            value = sufIdentifier,
                            onValueChange = { sufIdentifier = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.LightGray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        sufIdentifier = ""
                                    }
                                }
                                .focusRequester(focusRequester)
                                .width(IntrinsicSize.Min)
                        )
                    }

                    Text(
                        text = "모양",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 50.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 64.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(unselectedButtonColor)
                    ) {
                        Button(
                            onClick = { selectedShape = "타원" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedShape == "타원") selectedButtonColor else unselectedButtonColor
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Text(
                                text = "타원",
                                color = if (selectedShape == "타원") selectedButtonTextColor else unselectedButtonTextColor
                            )
                        }
                        Button(
                            onClick = { selectedShape = "원형" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedShape == "원형") selectedButtonColor else unselectedButtonColor
                            ),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = "원형",
                                color = if (selectedShape == "원형") selectedButtonTextColor else unselectedButtonTextColor
                            )
                        }
                        Button(
                            onClick = { selectedShape = "기타" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedShape == "기타") selectedButtonColor else unselectedButtonColor
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            Text(
                                text = "기타",
                                color = if (selectedShape == "기타") selectedButtonTextColor else unselectedButtonTextColor
                            )
                        }
                    }

                    Text(
                        text = "색상",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 50.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(unselectedButtonColor)
                    ) {
                        Button(
                            onClick = { selectedColor = "하양" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedColor == "하양") selectedButtonColor else unselectedButtonColor
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Text(
                                text = "하양",
                                color = if (selectedColor == "하양") selectedButtonTextColor else unselectedButtonTextColor
                            )
                        }
                        Button(
                            onClick = { selectedColor = "노랑" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedColor == "노랑") selectedButtonColor else unselectedButtonColor
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "노랑",
                                color = if (selectedColor == "노랑") selectedButtonTextColor else unselectedButtonTextColor
                            )
                        }
                        Button(
                            onClick = { selectedColor = "분홍" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedColor == "분홍") selectedButtonColor else unselectedButtonColor
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "분홍",
                                color = if (selectedColor == "분홍") selectedButtonTextColor else unselectedButtonTextColor
                            )
                        }
                        Button(
                            onClick = { selectedColor = "파랑" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedColor == "파랑") selectedButtonColor else unselectedButtonColor
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "파랑",
                                color = if (selectedColor == "파랑") selectedButtonTextColor else unselectedButtonTextColor
                            )
                        }
                        Button(
                            onClick = { selectedColor = "기타" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedColor == "기타") selectedButtonColor else unselectedButtonColor
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            Text(
                                text = "기타",
                                color = if (selectedColor == "기타") selectedButtonTextColor else unselectedButtonTextColor
                            )
                        }
                    }

                    androidx.compose.material.Button(
                        onClick = { navController.navigate(Routes.searchResult.route) },
                        colors = androidx.compose.material.ButtonDefaults.textButtonColors(
                            mainSearchColor
                        ),
                        modifier = Modifier
                            .size(328.dp, 60.dp)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 60.dp)
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

                androidx.compose.material.Button(
                    onClick = {
                        showImagePickerDialog = true
                    },
                    colors = androidx.compose.material.ButtonDefaults.textButtonColors(mainSearchColor),
                    modifier = Modifier
                        .size(328.dp, 60.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = "이미지 가져오기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

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

@Preview(showBackground = true)
@Composable
fun SearchMethodPreview() {
    val navController = rememberNavController()
    val searchViewModel:SearchViewModel = viewModel()
    searchViewModel.showSearch[0] = true
    SearchMethod(navController = navController, searchViewModel = searchViewModel)
}
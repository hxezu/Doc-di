package com.example.doc_di.home

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
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
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.etc.Routes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("")}

    var relationExpanded by remember{ mutableStateOf(false)}
    val relationOptions = listOf<String>("나", "보호자")
    var relation by remember { mutableStateOf(relationOptions[0])}

    var genderExpanded by remember{ mutableStateOf(false)}
    val genderOptions = listOf<String>("여성", "남성")
    var gender by remember { mutableStateOf(genderOptions[0])}

    var birthDate by remember { mutableStateOf("19990101")}

    var height by remember { mutableStateOf("")}
    var weight by remember { mutableStateOf("")}

    var bloodTypeExpanded by remember{ mutableStateOf(false)}
    val bloodTypeOptions = listOf<String>("A", "B", "O", "AB")
    var bloodType by remember { mutableStateOf(bloodTypeOptions[0])}

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

    val textFieldHeight = 60.dp
    val labelSize = 14.sp
    val labelColor = Color(0xFF747F9E)
    val spacer = 16.dp
    val textFieldWidth = 300.dp
    val textFieldHalfWidth= 140.dp
    val dropDownColor = Color(0xFF69BBBB)

    var showDatePicker by remember { mutableStateOf(false) }
    var showImagePickerDialog by remember{ mutableStateOf(false)}

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
            .padding(vertical = 48.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "뒤로가기",
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Start)
                .clickable { navController.popBackStack() }
        )

        Box(
            modifier = Modifier
                .size(124.dp)
                .clickable {
                    showImagePickerDialog = true
                }
                .clip(RoundedCornerShape(52.dp))
        ){
            if (imageBitmap != null){
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            else{
                Image(
                    painter = painterResource(id = R.drawable.user_image),
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
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

        OutlinedTextField(
            value = name,
            onValueChange = {name = it},
            label = {
                Text(
                    text = "이름",
                    fontSize = labelSize,
                    fontWeight = FontWeight.Bold,
                    color = labelColor,
                )
            },
            modifier = Modifier
                .size(width = textFieldWidth, height = textFieldHeight)
        )
        Row {
            Box {
                OutlinedTextField(
                    value = relation,
                    onValueChange = { relation = it },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "드롭다운",
                            tint = dropDownColor,
                            modifier = Modifier
                                .clickable {
                                    relationExpanded = true
                                }
                        )
                    },
                    label = { Text(text = "관계", fontSize = labelSize, color = labelColor, fontWeight = FontWeight.Bold) },
                    modifier = Modifier
                        .size(width = textFieldHalfWidth, height = textFieldHeight)
                )
                DropdownMenu(
                    expanded = relationExpanded,
                    onDismissRequest = { relationExpanded = false }
                ) {
                    relationOptions.forEach {
                        DropdownMenuItem(
                            onClick = {
                                relation = it
                                relationExpanded = false
                            }
                        ) {
                            Text(text = it)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(spacer))
            Box {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "드롭다운",
                            tint = dropDownColor,
                            modifier = Modifier
                                .clickable {
                                    genderExpanded = true
                                }
                        )
                    },
                    label = { Text(text = "성별", fontSize = labelSize, color = labelColor, fontWeight = FontWeight.Bold)},
                    modifier = Modifier
                        .size(width = textFieldHalfWidth, height = textFieldHeight)
                )
                DropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach {
                        DropdownMenuItem(
                            onClick = {
                                gender = it
                                genderExpanded = false
                            }
                        ) {
                            Text(text = it)
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "날짜 선택",
                    tint = dropDownColor,
                    modifier = Modifier
                        .clickable { showDatePicker = true}
                )
            },
            label = { Text(text = "생년월일", fontSize = labelSize, color = labelColor, fontWeight = FontWeight.Bold) },
            modifier = Modifier
                .size(width = textFieldWidth, height = textFieldHeight)
        )
        if (showDatePicker){
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = { },
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                val datePickerState = rememberDatePickerState(
                    yearRange = 1900..2024,
                    initialDisplayMode = DisplayMode.Picker,
                    initialSelectedDateMillis = birthDate.let {
                        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("UTC")
                        }
                        formatter.parse(it)?.time ?: System.currentTimeMillis()
                    }
                )
                DatePicker(state = datePickerState)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { showDatePicker = false }) {
                        Text(text = "취소")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                                val yyyyMMdd = SimpleDateFormat(
                                    "yyyyMMdd",
                                    Locale.getDefault()
                                ).format(Date(selectedDateMillis))
                                birthDate = yyyyMMdd
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text(text = "확인")
                    }
                }
            }
        }

        Row {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = {
                    Text(
                        text = "키(cm)",
                        fontSize = labelSize,
                        color = labelColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = Modifier
                    .size(width = textFieldHalfWidth, height = textFieldHeight)
            )
            Spacer(modifier = Modifier.width(spacer))
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = {
                    Text(
                        text = "몸무게(kg)",
                        fontSize = labelSize,
                        color = labelColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = Modifier
                    .size(width = textFieldHalfWidth, height = textFieldHeight)
            )
        }

        Box {
            OutlinedTextField(
                value = bloodType,
                onValueChange = { bloodType = it },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "드롭다운",
                        tint = dropDownColor,
                        modifier = Modifier
                            .clickable {
                                bloodTypeExpanded = true
                            }
                    )
                },
                label = { Text(text = "혈액형", fontSize = labelSize, color = labelColor, fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .size(width = textFieldWidth, height = textFieldHeight)
            )
            DropdownMenu(
                expanded = bloodTypeExpanded,
                onDismissRequest = { bloodTypeExpanded = false }
            ) {
                bloodTypeOptions.forEach {
                    DropdownMenuItem(
                        onClick = {
                            bloodType = it
                            bloodTypeExpanded = false
                        }
                    ) {
                        Text(text = it)
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(Routes.home.route) },
            colors = ButtonDefaults.textButtonColors(Color(0xFF007AEB)),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(text = "프로필 수정", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    Profile(navController = navController)
}
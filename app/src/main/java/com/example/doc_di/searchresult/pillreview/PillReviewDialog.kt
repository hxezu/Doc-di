package com.example.doc_di.searchresult.pillreview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillReviewDialog(
    onDismiss: () -> Unit,
) {
    var reviewText by remember { mutableStateOf("") }
    var curStarRating by remember { mutableIntStateOf(0) }

    val starYellow = Color(0xFFFFC107)
    val starGray = Color(0xFFE9EBED)
    val buttonColor = Color(0xFF4B7BE5)

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onDismiss() }
                                .align(Alignment.CenterStart)
                        )
                        Text(
                            text = "후기 작성",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }

                    Row(horizontalArrangement = Arrangement.Center) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "평점",
                                tint = if (curStarRating >= i) starYellow else starGray,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { curStarRating = i }
                            )
                            if (i in 1..4) {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "상세 리뷰",
                            color = Color(0xFF9CA4AB),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = { reviewText = it },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = buttonColor,
                                unfocusedBorderColor = buttonColor
                            ),
                            shape = RoundedCornerShape(24.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            maxLines = 4,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(136.dp)
                                .padding(horizontal = 20.dp)
                        )
                    }

                    Button(
                        onClick = { onDismiss() },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(buttonColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = "작성 완료", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
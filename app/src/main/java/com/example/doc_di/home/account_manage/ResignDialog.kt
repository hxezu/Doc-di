package com.example.doc_di.home.account_manage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.doc_di.ui.theme.VeryLightBlue

@Composable
fun ResignDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        icon = { Icon(Icons.Default.Warning, contentDescription = "주의") },
        title = { Text(text = "계정을 삭제 하시겠습니까?") },
        text = {
            Text(
                text = "확인 버튼 클릭 시, 계정의 모든 내용은 일체 사라지며, 다시 복구되지 않습니다",
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = { onDismissRequest() },
        containerColor = VeryLightBlue,
        iconContentColor = Color.Black,
        titleContentColor = Color.Black,
        textContentColor = Color.Gray,
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text("확인", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text("취소", color = Color.Black)
            }
        }
    )
}
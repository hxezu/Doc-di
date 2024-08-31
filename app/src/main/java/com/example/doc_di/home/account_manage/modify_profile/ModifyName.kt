import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.doc_di.ui.theme.MainBlue

@Composable
fun ModifyName(name: MutableState<String>, isNameAvailable: MutableState<Boolean>) {
    var userNameError by rememberSaveable { mutableStateOf<String?>(null) }
    val nameRegex = "^[가-힣a-zA-Z]{1,10}$".toRegex()

    val labelSize = 14.sp
    val labelColor = Color(0xFF747F9E)

    OutlinedTextField(
        value = name.value,
        onValueChange = {
            name.value = it
            userNameError = when {
                it.isEmpty() -> {
                    isNameAvailable.value = false
                    "이름을 입력해주세요."
                }
                !nameRegex.matches(name.value) -> {
                    isNameAvailable.value = false
                    "영문, 한글만 10자 이내로 기재해주세요."
                }
                else -> {
                    isNameAvailable.value = true
                    null
                }
            }},
        label = {
            Text(
                text = "이름",
                fontSize = labelSize,
                fontWeight = FontWeight.Bold,
                color = labelColor,
            )
        },
        placeholder = { Text(text = "영문, 한글 10자 이내로 기재") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            cursorColor = MainBlue
        ),
        modifier = Modifier.fillMaxWidth(0.9f)
    )
    if (!userNameError.isNullOrEmpty()) {
        Text(text = userNameError!!, color = Color.Red)
    }
}
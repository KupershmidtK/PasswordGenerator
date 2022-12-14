package com.example.passwordgenerator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordgenerator.*
import com.example.passwordgenerator.R
import com.example.passwordgenerator.data.PassStateHolder
import com.example.passwordgenerator.ui.theme.PasswordGeneratorTheme
import kotlin.math.nextTowards
import kotlin.math.roundToInt



@Composable
fun PasswordCard(viewModel: PasswordViewModel) {

    val uiState = viewModel.uiState.collectAsState().value

//    val scaffoldState = rememberScaffoldState()
//    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {

        Password(viewModel.password)
        Spacer(modifier = Modifier.size(16.dp))

        PasswordComplexityWithChips(action = { viewModel.setComplexity(it) })
        Spacer(modifier = Modifier.size(16.dp))

        PassLengthSlider(uiState.value.passLength
        ) { viewModel.setLength(it) }
        Spacer(modifier = Modifier.size(16.dp))

        CheckBoxAndText(
            text = stringResource(id = R.string.lower_case_txt),
            handler = { viewModel.setLowerLetters(it) },
            uiState.value.useLowerLetters
        )
        CheckBoxAndText(
            text = stringResource(id = R.string.upper_case_txt),
            handler = { viewModel.setUpperLetters(it) },
            uiState.value.useUpperLetters
        )
        CheckBoxAndText(
            text = stringResource(id = R.string.numbers_txt),
            handler = { viewModel.setNumbers(it) },
            uiState.value.useNumbers
        )
        CheckBoxAndText(
            text = stringResource(id = R.string.symbols_txt),
            { viewModel.setSymbols(it) },
            uiState.value.useSymbols
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PasswordComplexityWithChips(action: (PassStateHolder) -> Unit) {
    Row {
        Chip(
            modifier = Modifier
                .weight(1f, true),
            onClick = { action(PassStateHolder.Hard) }) {
            Text(stringResource(id = R.string.hard_pass_text))
        }
        Chip(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            onClick = { action(PassStateHolder.Medium) }) {
            Text(stringResource(id = R.string.medium_pass_text))
        }
        Chip(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            onClick = { action(PassStateHolder.Easy) }) {
            Text(stringResource(id = R.string.easy_pass_text))
        }
        Chip(
            modifier = Modifier
                .weight(1f),
            onClick = { action(PassStateHolder.PIN) }) {
            Text(stringResource(id = R.string.pincode_text))
        }

    }
}

@Composable
fun CheckBoxAndText(text: String, handler: (Boolean) -> Unit, checked: Boolean = false, enabled: Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = handler,
            Modifier.padding(end = 16.dp),
            enabled = enabled
        )

        Text(text)
    }
}

@Composable
fun Password(password: String) {
    Text(
        text = password,
        fontSize = 48.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordLength() {
    var passLength by rememberSaveable { mutableStateOf(4) }
    var isError by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        TextField(
            value = if (passLength != 0) passLength.toString() else "",
            onValueChange = {
                passLength = it.toIntOrNull() ?: 0
                isError = passLength < 4 || passLength > 20
            },
            label = { Text(stringResource(id = R.string.pass_length_txt)) },
            singleLine = true,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if(isError) {
            Text(
                text = "Error message",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .height(16.dp)
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun PassLengthSlider(passLength: Int, setLength: (Int) -> Unit) {
    val range = PassStateHolder.PIN.value.passLength.toFloat() .. PassStateHolder.Hard.value.passLength.toFloat()
    val steps = range.endInclusive - range.start - 1

    //var value by rememberSaveable { mutableStateOf(passLength.toFloat()) }
//    var value by remember { mutableStateOf( passLength.toFloat()) }
    var value = remember { passLength.toFloat() }
//    var value by remember { mutableStateOf(8f) }
//    var valueText by rememberSaveable { mutableStateOf(value.toInt()) }
    //var valueText = value.toInt()

    Column() {
        Row() {
            Text(text = stringResource(id = R.string.pass_length_txt))
            Text(text = passLength.toString())
        }
        Slider(
            value = passLength.toFloat(),
            onValueChange = {
//                value = it
                setLength (it.roundToInt())
                //valueText = it.roundToInt()
                //setLength( valueText )

            },
            //onValueChangeFinished = { setLength (value.roundToInt()) },
            valueRange = range,
            steps = steps.toInt()
        )
    }

}

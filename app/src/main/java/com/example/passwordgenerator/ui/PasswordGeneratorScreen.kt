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
import com.example.passwordgenerator.PassComplexityEnum
import com.example.passwordgenerator.PasswordViewModel
import com.example.passwordgenerator.R
import com.example.passwordgenerator.ui.theme.PasswordGeneratorTheme
import kotlin.math.roundToInt

@Composable
fun PassGenScreen() {

    val viewModel: PasswordViewModel = viewModel()

    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = Modifier,
        topBar = { Text(text = stringResource(id = R.string.app_name)) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(id = R.string.btn_generate_txt)) },
                onClick = { viewModel.generate() }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(padding),
            color = MaterialTheme.colors.background
        ) {
            PasswordCard(viewModel)
        }
    }
}

@Composable
fun PasswordCard(viewModel: PasswordViewModel) {

    val uiState = viewModel.uiState

//    val scaffoldState = rememberScaffoldState()
//    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {

        Password(viewModel.password)
        Spacer(modifier = Modifier.size(16.dp))

        PasswordComplexityWithChips(action = { viewModel.setComplexity(it) })
        Spacer(modifier = Modifier.size(16.dp))

        PassLengthSlider(uiState.passLength
        ) { viewModel.setLength(it) }
        Spacer(modifier = Modifier.size(16.dp))

        CheckBoxAndText(
            text = stringResource(id = R.string.lower_case_txt),
            handler = { viewModel.setLowerLetters(it) },
            uiState.useLowerLetters
        )
        CheckBoxAndText(
            text = stringResource(id = R.string.upper_case_txt),
            handler = { viewModel.setUpperLetters(it) },
            uiState.useUpperLetters
        )
        CheckBoxAndText(
            text = stringResource(id = R.string.numbers_txt),
            handler = { viewModel.setNumbers(it) },
            uiState.useNumbers
        )
        CheckBoxAndText(
            text = stringResource(id = R.string.symbols_txt),
            { viewModel.setSymbols(it) },
            uiState.useSymbols
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PasswordComplexityWithChips(action: (PassComplexityEnum) -> Unit) {
    Row {
        Chip(
            modifier = Modifier
                .weight(1f, true),
            onClick = { action(PassComplexityEnum.HARD) }) {
            Text(stringResource(id = R.string.hard_pass_text))
        }
        Chip(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            onClick = { action(PassComplexityEnum.MEDIUM) }) {
            Text(stringResource(id = R.string.medium_pass_text))
        }
        Chip(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            onClick = { action(PassComplexityEnum.EASY) }) {
            Text(stringResource(id = R.string.easy_pass_text))
        }
        Chip(
            modifier = Modifier
                .weight(1f),
            onClick = { action(PassComplexityEnum.PIN) }) {
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
    val range = 4f .. 12f
    val steps = range.endInclusive - range.start - 1

    //var value by rememberSaveable { mutableStateOf(passLength.toFloat()) }
    var value = passLength.toFloat()
//    var value by remember { mutableStateOf(8f) }
//    var valueText by rememberSaveable { mutableStateOf(value.toInt()) }
    var valueText = value.toInt()

    Column() {
        Row() {
            Text(text = stringResource(id = R.string.pass_length_txt))
            Text(text = valueText.toString())
        }
        Slider(
            value = value,
            onValueChange = {
                //value = it
                valueText = it.roundToInt()
                setLength( valueText )

            },
            //onValueChangeFinished = { setLength (valueText) },
            valueRange = range,
            steps = steps.toInt()
        )
    }

}

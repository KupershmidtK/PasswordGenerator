package com.example.passwordgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordgenerator.ui.theme.PasswordGeneratorTheme
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasswordGeneratorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PasswordCard()
                }
            }
        }
    }
}

@Composable
fun PasswordCard(viewModel: PasswordViewModel = viewModel()) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.padding(8.dp),
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Generate") },
                onClick = {
                    viewModel.generate()
                    // show snackbar as a suspend function
                    /*scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            password
                        )
                    }*/
                }
            )
        },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
//        Divider()

            Password(viewModel.uiState.password.value)
            Spacer(modifier = Modifier.size(16.dp))

//        PasswordComplexity()
            PasswordComplexityWithChips(action = { viewModel.setComplexity(it) })
            Spacer(modifier = Modifier.size(16.dp))

//        PasswordLength()
            PassLengthSlider(
                viewModel.uiState.passLength.value,
                { viewModel.uiState.passLength.value = it })
            Spacer(modifier = Modifier.size(16.dp))

            //PasswordParameters()
            CheckBoxAndText(
                text = "a - z",
                handler = { viewModel.uiState.useLowerLetters.value = it },
                viewModel.uiState.useLowerLetters.value
            )
            CheckBoxAndText(
                text = "A - Z",
                handler = { viewModel.uiState.useUpperLetters.value = it },
                viewModel.uiState.useUpperLetters.value
            )
            CheckBoxAndText(
                text = "0 - 9",
                handler = { viewModel.uiState.useNumbers.value = it },
                viewModel.uiState.useNumbers.value
            )
            CheckBoxAndText(
                text = "!, @, #, ...",
                { viewModel.uiState.useSymbols.value = it },
                viewModel.uiState.useSymbols.value
            )
        }
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
            Text("Hard")
        }
        Chip(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            onClick = { action(PassComplexityEnum.MEDIUM) }) {
            Text("Medium")
        }
        Chip(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            onClick = { action(PassComplexityEnum.EASY) }) {
            Text("Easy")
        }
        Chip(
            modifier = Modifier
                .weight(1f),
            onClick = { action(PassComplexityEnum.PIN) }) {
            Text("PIN")
        }

    }
}

@Composable
fun PasswordParameters() {
    var lowLetters by rememberSaveable { mutableStateOf(true) }
    var highLetters by remember { mutableStateOf(true) }
    var numbers by remember { mutableStateOf(true) }
    var symbols by remember { mutableStateOf(false) }

    Column() {
        CheckBoxAndText(text = "a - z", handler = { lowLetters = it }, lowLetters )
        CheckBoxAndText(text = "A - Z", handler = { highLetters = it }, highLetters)
        CheckBoxAndText(text = "0 - 9", handler = { numbers = it }, numbers)
        CheckBoxAndText(text = "!, @, #, ...", handler = { symbols = it }, symbols, false)
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
            label = { Text("Password length") },
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
    //var value by rememberSaveable { mutableStateOf(passLength.toFloat()) }
    var value = passLength.toFloat()
//    var value by remember { mutableStateOf(8f) }
//    var valueText by rememberSaveable { mutableStateOf(value.toInt()) }
    var valueText = value.toInt()

    Column() {
        Row() {
            Text(text = "Password length ")
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
            valueRange = 4f .. 15f,
            steps = 10
        )
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PasswordGeneratorTheme {
        PasswordCard()
    }
}
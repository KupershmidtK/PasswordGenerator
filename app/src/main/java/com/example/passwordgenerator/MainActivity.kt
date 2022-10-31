package com.example.passwordgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordgenerator.ui.theme.PasswordGeneratorTheme



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
fun PasswordCard() {

    Column (modifier = Modifier.padding(8.dp)) {
//        Divider()

        Password()
        Spacer(modifier = Modifier.size(16.dp))

//        PasswordComplexity()
        PasswordComplexityWithChips()
        Spacer(modifier = Modifier.size(16.dp))

//        PasswordLength()
        PassLengthSlider()
        Spacer(modifier = Modifier.size(16.dp))

        PasswordParameters()
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PasswordComplexityWithChips() {
    Row {
        Chip(
            modifier = Modifier
                .weight(1f, true),
            onClick = { /* do something*/ }) {
            Text("Hard")
        }
        Chip(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            onClick = { /* do something*/ }) {
            Text("Medium")
        }
        Chip(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            onClick = { /* do something*/ }) {
            Text("Easy")
        }
        Chip(
            modifier = Modifier
                .weight(1f),
            onClick = { /* do something*/ }) {
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
fun Password() {
    Text(
        text = "PASSWORD",
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
fun PassLengthSlider() {
    var value by rememberSaveable { mutableStateOf(8f) }
    var valueText by rememberSaveable { mutableStateOf(value.toInt()) }

    Column() {
        Row() {
            Text(text = "Password length ")
            Text(text = valueText.toString())
        }
        Slider(
            value = value,
            onValueChange = {
                value = it
                valueText = it.toInt()
            },
//            onValueChangeFinished = { valueText = value.toInt() },
            valueRange = 4f .. 20f,
            steps = 15
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
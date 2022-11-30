package com.example.passwordgenerator.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordgenerator.PasswordViewModel
import com.example.passwordgenerator.R
import kotlinx.coroutines.launch

@Composable
fun PassApp() {

    val viewModel: PasswordViewModel = viewModel()

    // A surface container using the 'background' color from the theme
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        topBar = { Text(text = stringResource(id = R.string.app_name)) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(id = R.string.btn_generate_txt)) },
                onClick = {
                    try {
                        viewModel.generate()
                    } catch (_: Exception) {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Check at least one parameter",
                                actionLabel = "Error"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colors.background
        ) {
            PasswordCard(viewModel)
        }
    }
}
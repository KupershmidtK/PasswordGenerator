package com.example.passwordgenerator

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

enum class PassComplexityEnum {
    HARD,
    MEDIUM,
    EASY,
    PIN,
    CUSTOM
}

//data class PasswordStateHolder (
//    var password: MutableState<String> = mutableStateOf("PASSWORD"),
//    var passComplexity: MutableState<PassComplexityEnum> = mutableStateOf(PassComplexityEnum.MEDIUM),
//    var passLength: MutableState<Int> = mutableStateOf(8),
//    var useLowerLetters: MutableState<Boolean> = mutableStateOf(true),
//    var useUpperLetters: MutableState<Boolean> = mutableStateOf(true),
//    var useNumbers: MutableState<Boolean> = mutableStateOf(true),
//    var useSymbols: MutableState<Boolean> = mutableStateOf(true),
//)
data class PasswordStateHolder (
//    var password: String = "PASSWORD",
    val passComplexity: PassComplexityEnum = PassComplexityEnum.MEDIUM,
    val passLength: Int = 8,
    val useLowerLetters: Boolean = true,
    val useUpperLetters: Boolean = true,
    val useNumbers: Boolean = true,
    val useSymbols: Boolean = true,
)

class PasswordViewModel: ViewModel() {
//    private val _uiState = mutableStateOf(PasswordStateHolder())
//    val uiState: State<PasswordStateHolder>
//        get() = _uiState

//    var uiState = MutableStateFlow(PasswordStateHolder())
//        private set

    var uiState by mutableStateOf(PasswordStateHolder())
        private set

    var password by mutableStateOf("PASSWORD")
        private set

    fun generate() {
        password = PasswordGenerator.generate(
            uiState.passLength,
            uiState.useLowerLetters,
            uiState.useUpperLetters,
            uiState.useNumbers,
            uiState.useSymbols
        )
    }

    fun setLength(length: Int) {
        uiState = uiState.copy(passComplexity = PassComplexityEnum.CUSTOM, passLength = length)
    }

    fun setLowerLetters(flag: Boolean) {
        uiState = uiState.copy(passComplexity = PassComplexityEnum.CUSTOM, useLowerLetters = flag)
    }

    fun setUpperLetters(flag: Boolean) {
        uiState = uiState.copy(passComplexity = PassComplexityEnum.CUSTOM, useUpperLetters = flag)
    }

    fun setNumbers(flag: Boolean) {
        uiState = uiState.copy(passComplexity = PassComplexityEnum.CUSTOM, useNumbers = flag)
    }

    fun setSymbols(flag: Boolean) {
        uiState = uiState.copy(passComplexity = PassComplexityEnum.CUSTOM, useSymbols = flag)
    }

    fun setComplexity(complexity: PassComplexityEnum) {
        when(complexity) {
            PassComplexityEnum.HARD     -> { setState(length = 15, complexity = PassComplexityEnum.HARD) }
            PassComplexityEnum.MEDIUM   -> { setState() }
            PassComplexityEnum.EASY     -> { setState(length = 6, complexity = PassComplexityEnum.EASY, upperLetters = false, symbols = false) }
            PassComplexityEnum.PIN      -> { setState(length = 4, complexity = PassComplexityEnum.PIN, false, false, true, false) }
            PassComplexityEnum.CUSTOM -> {}
        }
    }

    private fun setState(
        length: Int = 8,
        complexity: PassComplexityEnum = PassComplexityEnum.MEDIUM,
        lowerLetters: Boolean = true,
        upperLetters: Boolean = true,
        numbers: Boolean = true,
        symbols: Boolean = true) {
            uiState = uiState.copy(
                complexity,
                length,
                lowerLetters,
                upperLetters,
                numbers,
                symbols
            )
    }
}
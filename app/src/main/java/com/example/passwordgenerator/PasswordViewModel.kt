package com.example.passwordgenerator

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

sealed class PasswordUIState(
    private val passLength: Int,
    private val useLowerLetters: Boolean,
    private val useUpperLetters: Boolean,
    private val useNumbers: Boolean,
    private val useSymbols: Boolean,
) {
    object HardPasswordUIState : PasswordUIState(12, true, true, true, true)
    object MediumPasswordUIState : PasswordUIState(8, true, true, true, true)
    object EasyPasswordUIState : PasswordUIState(6, true, false, true, false)
    object PinPasswordUIState : PasswordUIState(4, false, false, true, false)
//    data class CustomPasswordUIState(4, false, false, true, false) : PasswordUIState(4, false, false, true, false)

}

enum class PassComplexityEnum {
    HARD,
    MEDIUM,
    EASY,
    PIN,
    CUSTOM
}
data class PasswordStateHolder (
    var password: MutableState<String> = mutableStateOf("PASSWORD"),
    var passComplexity: MutableState<PassComplexityEnum> = mutableStateOf(PassComplexityEnum.MEDIUM),
    var passLength: MutableState<Int> = mutableStateOf(8),
    var useLowerLetters: MutableState<Boolean> = mutableStateOf(true),
    var useUpperLetters: MutableState<Boolean> = mutableStateOf(true),
    var useNumbers: MutableState<Boolean> = mutableStateOf(true),
    var useSymbols: MutableState<Boolean> = mutableStateOf(true),
)

class PasswordViewModel: ViewModel() {
    private val _uiState = mutableStateOf(PasswordStateHolder())
    val uiState: State<PasswordStateHolder>
        get() = _uiState


    fun generate() {
        _uiState.value.password.value = PasswordGenerator.generate(
            _uiState.value.passLength.value,
            _uiState.value.useLowerLetters.value,
            _uiState.value.useUpperLetters.value,
            _uiState.value.useNumbers.value,
            _uiState.value.useSymbols.value
        )
    }

    fun setLength(length: Int) {
        _uiState.value.passLength.value = length
    }

    fun setComplexity(complexity: PassComplexityEnum) {
        when(complexity) {
            PassComplexityEnum.HARD -> {
                setState(length = 15, complexity = PassComplexityEnum.HARD)
            }
            PassComplexityEnum.MEDIUM -> {
                setState(length = 8)
            }
            PassComplexityEnum.EASY -> {
                setState(length = 6, complexity = PassComplexityEnum.EASY, upperLetters = false, symbols = false)
            }
            PassComplexityEnum.PIN -> {
                setState(length = 4, complexity = PassComplexityEnum.PIN, false, false, true, false)
            }
            else -> {}
        }
    }

    private fun setState(
        length: Int = 8,
        complexity: PassComplexityEnum = PassComplexityEnum.MEDIUM,
        lowerLetters: Boolean = true,
        upperLetters: Boolean = true,
        numbers: Boolean = true,
        symbols: Boolean = true) {
            _uiState.value.apply {
                passLength.value = length
                useLowerLetters.value = lowerLetters
                useUpperLetters.value = upperLetters
                useNumbers.value = numbers
                useSymbols.value = symbols
                passComplexity.value = complexity
            }
    }
}
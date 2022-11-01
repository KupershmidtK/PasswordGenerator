package com.example.passwordgenerator

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class PassComplexityEnum {
    HARD,
    MEDIUM,
    EASY,
    PIN,
    CUSTOM
}
data class PasswordStateHolder (
    var password: String = "PASSWORD",
    var passComplexity: MutableState<PassComplexityEnum> = mutableStateOf(PassComplexityEnum.MEDIUM),
    var passLength: MutableState<Int> = mutableStateOf(8),
    var useLowerLetters: MutableState<Boolean> = mutableStateOf(true),
    var useUpperLetters: MutableState<Boolean> = mutableStateOf(true),
    var useNumbers: MutableState<Boolean> = mutableStateOf(true),
    var useSymbols: MutableState<Boolean> = mutableStateOf(true),
)

class PasswordViewModel: ViewModel() {
    val uiState by mutableStateOf(PasswordStateHolder())


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
            uiState.apply {
                passLength.value = length
                useLowerLetters.value = lowerLetters
                useUpperLetters.value = upperLetters
                useNumbers.value = numbers
                useSymbols.value = symbols
                passComplexity.value = complexity
            }
    }
}
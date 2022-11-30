package com.example.passwordgenerator

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.passwordgenerator.data.PassStateHolder
import com.example.passwordgenerator.data.PasswordStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
/*
enum class PassComplexityEnum {
    HARD,
    MEDIUM,
    EASY,
    PIN,
    CUSTOM
}
*/
//data class PasswordStateHolder (
//    var password: MutableState<String> = mutableStateOf("PASSWORD"),
//    var passComplexity: MutableState<PassComplexityEnum> = mutableStateOf(PassComplexityEnum.MEDIUM),
//    var passLength: MutableState<Int> = mutableStateOf(8),
//    var useLowerLetters: MutableState<Boolean> = mutableStateOf(true),
//    var useUpperLetters: MutableState<Boolean> = mutableStateOf(true),
//    var useNumbers: MutableState<Boolean> = mutableStateOf(true),
//    var useSymbols: MutableState<Boolean> = mutableStateOf(true),
//)
/*
data class PasswordStateHolder (
//    var password: String = "PASSWORD",
//    val passComplexity: PassComplexityEnum = PassComplexityEnum.MEDIUM,
    val passLength: Int = 8,
    val useLowerLetters: Boolean = true,
    val useUpperLetters: Boolean = true,
    val useNumbers: Boolean = true,
    val useSymbols: Boolean = true,
)
*/

/*
sealed interface PassStateHolder {
    val value: PasswordStateHolder

    data class Hard(override val value: PasswordStateHolder = PasswordStateHolder(15)) : PassStateHolder
    data class Medium(override val value: PasswordStateHolder = PasswordStateHolder(8)) : PassStateHolder
    data class Easy(override val value: PasswordStateHolder = PasswordStateHolder(8, useUpperLetters  = false, useSymbols = false)) : PassStateHolder
    data class PIN (override val value: PasswordStateHolder = PasswordStateHolder(4, false, false, false, false)) : PassStateHolder
    data class Custom(override val value: PasswordStateHolder = PasswordStateHolder()) : PassStateHolder
}
*/


class PasswordViewModel: ViewModel() {
//    private val _uiState = mutableStateOf(PasswordStateHolder())
//    val uiState: State<PasswordStateHolder>
//        get() = _uiState

//    var uiState = MutableStateFlow(PasswordStateHolder())
//        private set


    private val _uiState: MutableStateFlow<PassStateHolder> = MutableStateFlow(PassStateHolder.Medium)
    val uiState: StateFlow<PassStateHolder> = _uiState.asStateFlow()

//    init {
//        uiState = _uiState.asStateFlow()
//    }


//    var uiState: PassStateHolder by mutableStateOf(PassStateHolder.Medium)
//        private set

    var password by mutableStateOf("PASSWORD")
        private set

    fun generate() {
        password = PasswordGenerator.generate(
            uiState.value.value.passLength,
            uiState.value.value.useLowerLetters,
            uiState.value.value.useUpperLetters,
            uiState.value.value.useNumbers,
            uiState.value.value.useSymbols
        )
    }

    fun setLength(length: Int) {
        //uiState = PassStateHolder.Custom(uiState.value.copy(passLength = length))
        _uiState.update {
            PassStateHolder.Custom(it.value.copy(passLength = length))
        }
    }

    fun setLowerLetters(flag: Boolean) {
//        uiState = PassStateHolder.Custom(uiState.value.copy(useLowerLetters = flag))
        _uiState.update {
            PassStateHolder.Custom(it.value.copy(useLowerLetters = flag))
        }
    }

    fun setUpperLetters(flag: Boolean) {
//        uiState = PassStateHolder.Custom(uiState.value.copy(useUpperLetters = flag))
        _uiState.update {
            PassStateHolder.Custom(it.value.copy(useUpperLetters = flag))
        }
    }

    fun setNumbers(flag: Boolean) {
//        uiState = PassStateHolder.Custom(uiState.value.copy(useNumbers = flag))
        _uiState.update {
            PassStateHolder.Custom(it.value.copy(useNumbers = flag))
        }
    }

    fun setSymbols(flag: Boolean) {
//        uiState = PassStateHolder.Custom(uiState.value.copy(useSymbols = flag))
        _uiState.update {
            PassStateHolder.Custom(it.value.copy(useSymbols = flag))
        }
    }

//    fun setComplexity(complexity: PassComplexityEnum) {
//        when(complexity) {
//            PassComplexityEnum.HARD     -> { setState(length = 15, complexity = PassComplexityEnum.HARD) }
//            PassComplexityEnum.MEDIUM   -> { setState() }
//            PassComplexityEnum.EASY     -> { setState(length = 6, complexity = PassComplexityEnum.EASY, upperLetters = false, symbols = false) }
//            PassComplexityEnum.PIN      -> { setState(length = 4, complexity = PassComplexityEnum.PIN, false, false, true, false) }
//            PassComplexityEnum.CUSTOM -> {}
//        }
//    }

    fun setComplexity(complexity: PassStateHolder) {
        _uiState.update { complexity }
    }
/*
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
*/
}



package com.example.passwordgenerator.data

data class PasswordStateHolder (
//    var password: String = "PASSWORD",
//    val passComplexity: PassComplexityEnum = PassComplexityEnum.MEDIUM,
    val passLength: Int = 8,
    val useLowerLetters: Boolean = true,
    val useUpperLetters: Boolean = true,
    val useNumbers: Boolean = true,
    val useSymbols: Boolean = true,
)

sealed class PassStateHolder(val value: PasswordStateHolder) {
    object Hard : PassStateHolder(PasswordStateHolder(12))
    object Medium : PassStateHolder(PasswordStateHolder())
    object Easy : PassStateHolder(PasswordStateHolder(7, useUpperLetters  = false, useSymbols = false))
    object PIN  : PassStateHolder(PasswordStateHolder(4, false, false, true, false))
    data class Custom(val v: PasswordStateHolder = PasswordStateHolder()) : PassStateHolder(v)
}
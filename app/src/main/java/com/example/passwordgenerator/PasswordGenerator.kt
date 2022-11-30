package com.example.passwordgenerator

object PasswordGenerator  {
    private val lowerCase: String = "abcdefghijklmnopqrstuvwxyz"
    private val upperCase: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val numbers: String = "0123456789"
    private val symbols: String = "!@#$%^&*"

    fun generate(
        length: Int = 8,
        lowerLetters: Boolean = true,
        upperLetters: Boolean = true,
        numbers: Boolean = true,
        symbols: Boolean = true): String {

        val result: MutableList<Char> = mutableListOf()

        var selection = "";
        selection += if (lowerLetters) lowerCase else "";
        selection += if (upperLetters) upperCase else "";
        selection += if (numbers) this.numbers else "";
        selection += if (symbols) this.symbols else "";

        if (selection.isBlank()) throw Exception()

        repeat(length) {
            result.add(selection.random())
        }
        result.shuffle()
        return result.joinToString(separator = "")
    }

}
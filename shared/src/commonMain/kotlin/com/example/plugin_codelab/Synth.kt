package com.example.plugin_codelab

expect class Synth() {
    fun start()

    fun keyDown(key: Int): Int

    fun keyUp(key: Int): Int
}
package com.example.plugin_codelab

import cocoapods.MusicKeyboard.*
import platform.Foundation.NSLog
import kotlin.native.concurrent.freeze

actual class Synth {
    private val synth: FLRSynthRef? = FLRSynthCreate().freeze()
    private var mNumKeysDown = 0

    actual fun start() {
        NSLog("start:$synth")
        FLRSynthStart(synth)
    }

    actual fun keyDown(key: Int): Int {
        NSLog("keyDown:$key")
        FLRSynthKeyDown(synth, key)
        mNumKeysDown += 1
        return mNumKeysDown
    }

    actual fun keyUp(key: Int): Int {
        NSLog("keyUp:$key")
        FLRSynthKeyUp(synth, key)
        mNumKeysDown -= 1
        return mNumKeysDown
    }
}
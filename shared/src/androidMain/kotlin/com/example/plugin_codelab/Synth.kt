package com.example.plugin_codelab

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack

/** A synthesizer that plays sin waves for Android.  */
actual class Synth actual constructor() : Runnable {
    private var mThread: Thread? = null
    private var mRunning = false
    private var mFreq = 440.0
    private var mAmp = 0.0
    private var mNumKeysDown = 0

    actual fun start() {
        mThread = Thread(this)
        mRunning = true
        mThread?.start()
    }

    fun stop() {
        mRunning = false
    }

    actual fun keyDown(key: Int): Int {
        mFreq = Math.pow(1.0594630, (key - 69).toDouble()) * 440.0
        mAmp = 1.0
        mNumKeysDown += 1
        return mNumKeysDown
    }

    actual fun keyUp(key: Int): Int {
        mAmp = 0.0
        mNumKeysDown -= 1
        return mNumKeysDown
    }

    override fun run() {
        val sampleRate = 44100
        val bufferSize = 1024
        val buffer = ShortArray(bufferSize)
        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )
        val fSampleRate = sampleRate.toDouble()
        val pi2 = 2.0 * Math.PI
        var counter = 0.0
        audioTrack.play()
        while (mRunning) {
            val tau = pi2 * mFreq / fSampleRate
            val maxValue = Short.MAX_VALUE * mAmp
            for (i in 0 until bufferSize) {
                buffer[i] = (Math.sin(tau * counter) * maxValue).toInt().toShort()
                counter += 1.0
            }
            audioTrack.write(buffer, 0, bufferSize)
        }
        audioTrack.stop()
        audioTrack.release()
    }
}
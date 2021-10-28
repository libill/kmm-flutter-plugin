package com.example.plugin_codelab

class CommonCodelabPlugin {

    private val synth = Synth()

    init {
        synth?.start()
    }

    fun onMethodCall(call: CommonMethodCall, result: CommonMethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success(Platform().platform)
            }
            "onKeyDown" -> {
                try {
                    val arguments = call.arguments as List<*>
                    val numKeysDown = synth?.keyDown((arguments[0] as Int))
                    result.success(numKeysDown)
                } catch (ex: Exception) {
                    result.error("1", ex.message, ex.cause)
                }
            }
            "onKeyUp" -> {
                try {
                    val arguments = call.arguments as List<*>
                    val numKeysDown = synth?.keyUp((arguments[0] as Int))
                    result.success(numKeysDown)
                } catch (ex: Exception) {
                    result.error("1", ex.message, ex.cause)
                }
            }
            else -> {
                result.notImplemented()
            }
        }
    }
}
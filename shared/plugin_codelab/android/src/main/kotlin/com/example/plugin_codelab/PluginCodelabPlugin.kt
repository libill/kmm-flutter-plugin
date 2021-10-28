package com.example.plugin_codelab

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/** PluginCodelabPlugin  */
class PluginCodelabPlugin : FlutterPlugin, MethodCallHandler {
    private var channel: MethodChannel? = null
    private var commonCodelabPlugin: CommonCodelabPlugin? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        setup(this, flutterPluginBinding.binaryMessenger)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        commonCodelabPlugin?.onMethodCall(
            call = CommonMethodCall(call.method, call.arguments),
            result = object : CommonMethodChannel.Result {
                override fun success(successResult: Any?) {
                    result.success(successResult)
                }

                override fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?) {
                    result.error(errorCode, errorMessage, errorDetails)
                }

                override fun notImplemented() {
                    result.notImplemented()
                }
            })
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
    }

    companion object {
        private fun setup(plugin: PluginCodelabPlugin, binaryMessenger: BinaryMessenger) {
            plugin.channel = MethodChannel(binaryMessenger, PLUGIN_CODE_LAB_CHANNEL)
            plugin.channel?.setMethodCallHandler(plugin)
            plugin.commonCodelabPlugin = CommonCodelabPlugin()
        }
    }
}
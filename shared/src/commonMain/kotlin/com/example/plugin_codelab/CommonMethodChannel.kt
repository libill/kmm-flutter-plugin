package com.example.plugin_codelab

class CommonMethodChannel {
    interface Result {
        fun success(result: Any?)

        fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?)

        fun notImplemented()
    }
}
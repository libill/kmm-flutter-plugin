package com.example.plugin_codelab

class Greeting {
    fun greeting(): String {
        return "Hello,${Platform().platform}"
    }
}
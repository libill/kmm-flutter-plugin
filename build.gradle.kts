buildscript {
    repositories {
        maven(url = "http://download.flutter.io")
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
        maven(url ="https://jitpack.io")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${findProperty("version.kotlin")}")
        classpath("com.android.tools.build:gradle:4.1.3")
    }
}

allprojects {
    repositories {
        maven(url = "http://download.flutter.io")
        google()
        mavenCentral()
        jcenter()
        maven(url ="https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
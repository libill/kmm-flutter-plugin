import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "$version"

kotlin {
    android()
    val iosX64 = iosX64()
    val iosArm64 = iosArm64()
    targets {
        configure(listOf(iosX64, iosArm64)) {
            binaries.framework {
                baseName = "shared"
            }
        }
    }
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations.get("main").kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
    }

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.CInteropProcess::class.java) {
        settings.extraOpts(listOf("-compiler-option", "-DNS_FORMAT_ARGUMENT(A)="))
    }

    tasks.register<org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask>("releaseIOSFramework") {
        baseName = "shared"
        destinationDir = buildDir.resolve("cocoapods/framework")
        val isReleaseBuild = !"$version".contains("dev", ignoreCase = false)
        val buildType = if (isReleaseBuild) "RELEASE" else "DEBUG"
        from(
            iosArm64.binaries.getFramework(buildType),
            iosX64.binaries.getFramework(buildType)
        )
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        specRepos {
            url("https://github.com/CocoaPods/Specs.git")
            url("https://github.com/bytedance/cocoapods_sdk_source_repo.git")
            url("https://github.com/CocoaPods/Specs")
            url("https://mirrors.tuna.tsinghua.edu.cn/git/CocoaPods/Specs.git")
            url("https://cdn.cocoapods.org/")
        }
        ios.deploymentTarget = "13.5"

        frameworkName = "shared"

        pod("Flutter")

        pod("MusicKeyboard") {
            version = "1.0.0"
            source = path(project.file("../MusicKeyboard"))
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val androidMain by getting {
//            kotlin.srcDir("plugin_codelab/android/src/main/kotlin")
            dependencies {
                compileOnly("io.flutter:flutter_embedding_debug:1.0.0-07ba8fd075d61c9f19d73fbbc72288c7761401ab")
                compileOnly("io.flutter:arm64_v8a_debug:1.0.0-07ba8fd075d61c9f19d73fbbc72288c7761401ab")
                compileOnly("io.flutter:armeabi_v7a_debug:1.0.0-07ba8fd075d61c9f19d73fbbc72288c7761401ab")
                compileOnly("io.flutter:x86_64_debug:1.0.0-07ba8fd075d61c9f19d73fbbc72288c7761401ab")
                compileOnly("io.flutter:x86_debug:1.0.0-07ba8fd075d61c9f19d73fbbc72288c7761401ab")
            }
        }
        val iosX64Main by getting {
            kotlin.srcDir("src/iosMain")
            dependencies {
            }
        }
        val iosArm64Main by getting {
            dependsOn(iosX64Main)
        }
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(30)
    }
}

tasks.named<org.jetbrains.kotlin.gradle.tasks.DefFileTask>("generateDefFlutter").configure {
    doLast {
        println("generateDefFlutter start")
        val includeDir = File(projectDir, "build/cocoapods/synthetic/IOS/shared/Pods/Flutter/Flutter.xcframework/ios-arm64_armv7/Flutter.framework/Headers")
        val headers = listOf("${includeDir.path}/Flutter.h")
        headers.forEach {
            println("generateDefFlutter hearder:$it")
        }
        outputFile.writeText("""
            language = Objective-C
            headers = ${headers.joinToString(" ")}
             """
        )
    }
}
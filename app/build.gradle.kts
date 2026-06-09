plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.khanhminh.omnibottemplate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.khanhminh.omnibottemplate"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

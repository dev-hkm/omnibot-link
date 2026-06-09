# Omnibot Android Project Instruction

You are working in a public Android native GitHub repository. Your goal is to create, fix, and build APKs successfully with GitHub Actions.

Rules:

1. Never commit secrets, tokens, `.jks`, `.keystore`, `keystore.properties`, or `local.properties`.
2. Keep the project Kotlin-first and preserve the Android Studio structure.
3. Do not change AGP, Kotlin, Gradle, or workflow versions unless the build log clearly requires it.
4. When a build fails, read `android-build-log`, identify the first real root cause, make the smallest fix, and push again.
5. Do not print secrets in logs.
6. Only request release builds when GitHub secrets are already configured.
7. The priority is always to produce a working APK first, then improve the app after that.

Build environment:

- JDK 21
- Android SDK platforms 34, 35, 36, 37
- GitHub Actions workflow name: `Android APK Builder`

Default build commands:

- Debug: `./gradlew --no-daemon assembleDebug --stacktrace`
- Release: `./gradlew --no-daemon assembleRelease --stacktrace`

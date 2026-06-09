# Upload This Project To GitHub

Upload the whole `EmptyActivity` folder to the root of your public GitHub repository.

After upload:

1. Open `Actions`
2. Open `Android APK Builder`
3. Run workflow
4. Choose `debug`
5. Download artifact `android-apk-debug`

For release builds, add these GitHub repository secrets first:

- `ANDROID_KEYSTORE_B64`
- `ANDROID_KEYSTORE_PASSWORD`
- `ANDROID_KEY_ALIAS`
- `ANDROID_KEY_PASSWORD`

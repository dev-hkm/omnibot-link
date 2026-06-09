# Upload Folder Này Lên GitHub

Làm đúng 5 bước này:

1. Vào GitHub và tạo repo mới.
2. Chọn `Public`.
3. Bấm `uploading an existing file`.
4. Kéo thả toàn bộ file/folder bên trong `OMNIBOT_ANDROID_APK_TEMPLATE` lên GitHub.
5. Bấm `Commit changes`.

Sau đó build APK:

1. Vào tab `Actions`.
2. Chọn `Android APK Builder`.
3. Bấm `Run workflow`.
4. Chọn `debug`.
5. Đợi xanh rồi tải artifact `android-apk-debug`.

Không upload file keystore `.jks` lên repo public. Nếu muốn build release, dùng GitHub Secrets theo `SETUP_RELEASE.md`.

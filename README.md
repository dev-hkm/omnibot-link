# Omnibot Android APK Template

Repo mẫu Android native để Omnibot viết code và GitHub Actions tự build APK.

Bạn upload toàn bộ nội dung folder này lên một GitHub public repo. Sau đó vào tab `Actions` chạy workflow `Android APK Builder` để lấy APK.

## Có sẵn trong repo

- Android app Kotlin tối giản, build được bằng Gradle.
- GitHub Actions workflow build APK debug/release.
- Script cài Android SDK packages phổ biến.
- Script ký release APK bằng GitHub Secrets.
- Prompt riêng cho Omnibot để nó sửa code, đọc log và build lại đúng cách.

## Chỗ tải APK

Sau khi workflow chạy xong:

```text
GitHub repo -> Actions -> Android APK Builder -> run mới nhất -> Artifacts
```

Tải:

```text
android-apk-debug
```

hoặc:

```text
android-apk-release
```

## Prompt cho Omnibot

Đưa nội dung file `OMNIBOT_PROMPT.md` cho Omnibot làm instruction của project này.

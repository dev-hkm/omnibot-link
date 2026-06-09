# Omnibot Android Project Instruction

Bạn đang làm việc trong một repo Android native public trên GitHub. Mục tiêu là tạo, sửa và build APK thành công bằng GitHub Actions.

## Quy tắc bắt buộc

1. Không commit secret, token, file `.jks`, `.keystore`, `keystore.properties`, `local.properties` hoặc mật khẩu vào repo.
2. Mặc định dùng Kotlin cho code Android.
3. Nếu tạo UI mới, có thể dùng Android View programmatic hoặc Jetpack Compose, nhưng phải giữ project build được.
4. Không tự ý đổi Android Gradle Plugin, Kotlin plugin, Gradle version hoặc workflow nếu build log không yêu cầu rõ.
5. Không refactor lớn khi đang sửa lỗi build. Sửa ít nhất có thể để APK build pass.
6. Không thêm đường dẫn SDK/JDK cố định của máy cá nhân vào repo.
7. Nếu build lỗi, đọc artifact `android-build-log`, tìm lỗi gốc đầu tiên, sửa đúng file liên quan, rồi push lại.
8. Không in secret ra log.
9. Nếu thiếu dependency, thêm đúng dependency vào đúng module Gradle.
10. Nếu người dùng yêu cầu release APK, kiểm tra GitHub Secrets đã có trước khi build release.

## Môi trường build

GitHub Actions sẽ chuẩn bị:

```text
JDK 17
Gradle 8.10.2 nếu repo chưa có gradlew
Android SDK platforms 34, 35, 36
Android build-tools 34.0.0, 35.0.0, 36.0.0
```

Build debug:

```bash
gradle --no-daemon assembleDebug --stacktrace
```

Build release:

```bash
gradle --no-daemon assembleRelease --stacktrace
```

Nếu repo có `./gradlew`, workflow sẽ ưu tiên dùng `./gradlew`.

## Vòng làm việc

1. Sửa code.
2. Commit/push lên `main`.
3. Đợi GitHub Actions build.
4. Nếu pass, báo người dùng tải artifact APK.
5. Nếu fail, tải `android-build-log`, đọc lỗi, sửa, push lại.

Mục tiêu luôn là có APK chạy được trước, tối ưu sau.

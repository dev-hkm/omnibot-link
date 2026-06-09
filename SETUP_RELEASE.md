# Build Release APK Có Keystore

Chỉ làm bước này sau khi debug APK đã build thành công.

## 1. Encode keystore thành base64

Trên Windows PowerShell, chạy:

```powershell
powershell -ExecutionPolicy Bypass -File ".\scripts\encode-keystore.ps1" -Path "C:\duong-dan-toi-keystore\your-key.jks"
```

Copy kết quả một dòng rất dài.

## 2. Tạo GitHub Secrets

Vào repo GitHub:

```text
Settings -> Secrets and variables -> Actions -> New repository secret
```

Tạo đủ 4 secrets:

```text
ANDROID_KEYSTORE_B64
ANDROID_KEYSTORE_PASSWORD
ANDROID_KEY_ALIAS
ANDROID_KEY_PASSWORD
```

Tuyệt đối không commit file `.jks`, `.keystore`, mật khẩu hoặc token lên repo public.

## 3. Chạy release build

Vào:

```text
Actions -> Android APK Builder -> Run workflow
```

Chọn:

```text
build_type: release
```

Tải artifact:

```text
android-apk-release
```

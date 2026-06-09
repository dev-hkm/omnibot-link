#!/usr/bin/env bash
set -euo pipefail

mkdir -p artifacts/apk

mapfile -t APKS < <(find . -path "*/build/outputs/apk/*.apk" -type f | sort)
if [ "${#APKS[@]}" -eq 0 ]; then
  mapfile -t APKS < <(find . -path "*/build/outputs/apk/*/*.apk" -type f | sort)
fi

if [ "${#APKS[@]}" -eq 0 ]; then
  echo "No APK files found under build/outputs/apk." | tee -a build-log.txt
  exit 1
fi

BUILD_TYPE="${BUILD_TYPE:-debug}"
HAS_RELEASE_SECRETS="false"
if [ -n "${ANDROID_KEYSTORE_B64:-}" ] && [ -n "${ANDROID_KEYSTORE_PASSWORD:-}" ] && [ -n "${ANDROID_KEY_ALIAS:-}" ]; then
  HAS_RELEASE_SECRETS="true"
fi

if [ "$BUILD_TYPE" != "release" ] || [ "$HAS_RELEASE_SECRETS" != "true" ]; then
  if [ "$BUILD_TYPE" = "release" ]; then
    echo "Release keystore secrets are missing. Uploading the APK produced by Gradle without extra signing." | tee -a build-log.txt
  fi

  for apk in "${APKS[@]}"; do
    cp "$apk" "artifacts/apk/$(basename "$apk")"
  done
  exit 0
fi

KEYSTORE_FILE="${RUNNER_TEMP}/android-release.keystore"
printf "%s" "$ANDROID_KEYSTORE_B64" | base64 --decode > "$KEYSTORE_FILE"
chmod 600 "$KEYSTORE_FILE"

BUILD_TOOLS_DIR="$(find "$ANDROID_SDK_ROOT/build-tools" -mindepth 1 -maxdepth 1 -type d | sort -V | tail -n 1)"
ZIPALIGN="${BUILD_TOOLS_DIR}/zipalign"
APKSIGNER="${BUILD_TOOLS_DIR}/apksigner"

if [ ! -x "$ZIPALIGN" ] || [ ! -x "$APKSIGNER" ]; then
  echo "zipalign or apksigner was not found in Android build-tools." | tee -a build-log.txt
  exit 1
fi

KEY_PASSWORD="${ANDROID_KEY_PASSWORD:-$ANDROID_KEYSTORE_PASSWORD}"

for apk in "${APKS[@]}"; do
  base="$(basename "$apk" .apk)"
  aligned="artifacts/apk/${base}-aligned.apk"
  signed="artifacts/apk/${base}-signed.apk"

  "$ZIPALIGN" -p -f 4 "$apk" "$aligned"
  "$APKSIGNER" sign \
    --ks "$KEYSTORE_FILE" \
    --ks-pass "pass:${ANDROID_KEYSTORE_PASSWORD}" \
    --key-pass "pass:${KEY_PASSWORD}" \
    --ks-key-alias "$ANDROID_KEY_ALIAS" \
    --out "$signed" \
    "$aligned"

  "$APKSIGNER" verify --verbose "$signed" | tee -a build-log.txt
  rm -f "$aligned"
done

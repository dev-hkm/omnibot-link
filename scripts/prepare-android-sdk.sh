#!/usr/bin/env bash
set -euo pipefail

SDKMANAGER="${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager"
if [ ! -x "$SDKMANAGER" ]; then
  SDKMANAGER="$(command -v sdkmanager)"
fi

yes | "$SDKMANAGER" --licenses >/dev/null || true

"$SDKMANAGER" \
  "platform-tools" \
  "platforms;android-34" \
  "platforms;android-35" \
  "platforms;android-36" \
  "platforms;android-37" \
  "build-tools;35.0.0" \
  "build-tools;36.0.0"

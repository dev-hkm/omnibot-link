#!/usr/bin/env bash
set -euo pipefail

SDKMANAGER="${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager"
if [ ! -x "$SDKMANAGER" ]; then
  SDKMANAGER="$(command -v sdkmanager)"
fi

if [ -z "${SDKMANAGER:-}" ] || [ ! -x "$SDKMANAGER" ]; then
  echo "sdkmanager was not found on the GitHub runner."
  exit 1
fi

yes | "$SDKMANAGER" --licenses >/dev/null || true

"$SDKMANAGER" \
  "platform-tools" \
  "platforms;android-37" \
  "build-tools;35.0.0"

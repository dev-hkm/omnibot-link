#!/usr/bin/env bash
set -euo pipefail

if [ -x "./gradlew" ]; then
  GRADLE_CMD="./gradlew"
else
  GRADLE_CMD="$(command -v gradle || true)"
fi

if [ -z "${GRADLE_CMD}" ]; then
  echo "No Gradle command found. GitHub Actions should install Gradle before this step." | tee build-log.txt
  exit 2
fi

if [ -f "./gradlew" ]; then
  chmod +x ./gradlew
fi

BUILD_TYPE="${BUILD_TYPE:-debug}"
TASK="${GRADLE_TASK_INPUT:-}"
MODULE="${MODULE_INPUT:-}"

if [ -z "$TASK" ]; then
  if [ "$BUILD_TYPE" = "release" ]; then
    TASK="assembleRelease"
  else
    TASK="assembleDebug"
  fi
fi

if [[ "$TASK" == *" "* ]]; then
  echo "Only one Gradle task is allowed. Received: $TASK" | tee build-log.txt
  exit 2
fi

if [ -n "$MODULE" ] && [[ "$TASK" != :* ]] && [[ "$TASK" != *:* ]]; then
  TASK=":${MODULE}:${TASK}"
fi

echo "Running: $GRADLE_CMD --no-daemon $TASK --stacktrace" | tee build-log.txt
set -o pipefail
"$GRADLE_CMD" --no-daemon "$TASK" --stacktrace 2>&1 | tee -a build-log.txt

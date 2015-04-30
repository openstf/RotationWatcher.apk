#!/usr/bin/env bash

# Fail on error, verbose output
set -exo pipefail

# Install the APK
adb install -rd ./build/outputs/apk/*-debug.apk

# Figure out where it was installed
path=$(adb shell pm path jp.co.cyberagent.stf.rotationwatcher | tr -d '\r' | cut -d: -f 2)

# Run!
adb shell export CLASSPATH="$path"\; exec app_process /system/bin 'jp.co.cyberagent.stf.rotationwatcher.RotationWatcher' "$@"

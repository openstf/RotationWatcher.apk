#!/usr/bin/env bash
set -eo pipefail
tar tf libs/layoutlib.jar | grep -vE '^android/view/(IRotationWatcher|IWindowManager)|^android/os/ServiceManager' | xargs zip -d libs/layoutlib.jar
